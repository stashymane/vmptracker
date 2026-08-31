"""Reusable ExecuTorch export helpers (Vulkan + XNNPACK via Ultralytics)."""

from __future__ import annotations

import hashlib
import os
import shutil
import urllib.request
from contextlib import contextmanager
from datetime import datetime, timezone
from pathlib import Path
from typing import Any, Iterator

import torch
from ultralytics.utils import YAML as UltralyticsYAML


def sha256_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as f:
        for chunk in iter(lambda: f.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def download_file(url: str, dest: Path, *, expected_sha256: str | None = None) -> None:
    dest.parent.mkdir(parents=True, exist_ok=True)
    print(f"Downloading {url} ...")
    urllib.request.urlretrieve(url, dest)
    actual = sha256_file(dest)
    if expected_sha256 and actual != expected_sha256.lower():
        raise RuntimeError(
            f"SHA256 mismatch for {dest.name}: expected {expected_sha256}, got {actual}"
        )
    print(f"Saved {dest} ({dest.stat().st_size} bytes, sha256={actual})")


def torch2executorch_vulkan(
    model: torch.nn.Module,
    im: torch.Tensor,
    output_dir: Path | str,
    metadata: dict | None = None,
    prefix: str = "",
) -> str:
    from executorch import version as executorch_version
    from executorch.backends.vulkan.partitioner.vulkan_partitioner import VulkanPartitioner
    from executorch.exir import to_edge_transform_and_lower

    print(f"{prefix} starting Vulkan export with ExecuTorch {executorch_version.__version__}...")
    output_dir = Path(output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)

    pte_file = output_dir / "model.pte"
    et_program = to_edge_transform_and_lower(
        torch.export.export(model, (im,)),
        partitioner=[VulkanPartitioner({"force_fp16": True})],
    ).to_executorch()
    pte_file.write_bytes(et_program.buffer)

    if metadata is not None:
        UltralyticsYAML.save(output_dir / "metadata.yaml", metadata)

    return str(output_dir)


@contextmanager
def ultralytics_vulkan_export_hook() -> Iterator[None]:
    import ultralytics.utils.export.executorch as et_export

    original = et_export.torch2executorch
    et_export.torch2executorch = torch2executorch_vulkan
    try:
        yield
    finally:
        et_export.torch2executorch = original


def _copy_export_artifacts(export_root: Path, dest_dir: Path) -> Path:
    dest_dir.mkdir(parents=True, exist_ok=True)
    pte_src = export_root / "model.pte"
    if not pte_src.is_file():
        raise RuntimeError(f"Export missing {pte_src}")
    shutil.copy2(pte_src, dest_dir / "model.pte")
    metadata = export_root / "metadata.yaml"
    if metadata.is_file():
        shutil.copy2(metadata, dest_dir / "metadata.yaml")
    return dest_dir / "model.pte"


def export_ultralytics_executorch(
    yolo: Any,
    *,
    imgsz: int,
    work_dir: Path,
    batch: int = 1,
    device: str = "cpu",
) -> tuple[Path, Path]:
    """Export an Ultralytics model to XNNPACK and Vulkan .pte files."""

    vulkan_out = work_dir / "vulkan"
    xnnpack_out = work_dir / "xnnpack"
    vulkan_out.mkdir(parents=True, exist_ok=True)
    xnnpack_out.mkdir(parents=True, exist_ok=True)

    vulkan_root: Path | None = None
    xnnpack_root: Path | None = None
    cwd = Path.cwd()
    try:
        os.chdir(work_dir)
        with ultralytics_vulkan_export_hook():
            vulkan_root = Path(
                yolo.export(format="executorch", imgsz=imgsz, batch=batch, device=device)
            ).resolve()
        vulkan_pte = _copy_export_artifacts(vulkan_root, vulkan_out)

        xnnpack_root = Path(
            yolo.export(format="executorch", imgsz=imgsz, batch=batch, device=device)
        ).resolve()
        xnnpack_pte = _copy_export_artifacts(xnnpack_root, xnnpack_out)
    finally:
        os.chdir(cwd)

    for root in (vulkan_root, xnnpack_root):
        if root is not None and root.is_dir() and root.parent.resolve() == work_dir.resolve():
            shutil.rmtree(root, ignore_errors=True)

    return xnnpack_pte, vulkan_pte


def smoke_test_pte(pte_path: Path, example: torch.Tensor) -> None:
    try:
        from executorch.extension.pybindings.portable_lib import _load_for_executorch
    except ImportError:
        print("Skipping smoke test: executorch pybindings unavailable")
        return

    program = _load_for_executorch(str(pte_path))
    outputs = program.run_method("forward", [example])
    if not outputs:
        raise RuntimeError(f"Smoke test produced no outputs for {pte_path}")
    print(f"Smoke test OK: {pte_path.name} -> {len(outputs)} output(s)")


def build_export_metadata(*, imgsz: int) -> dict[str, Any]:
    import ultralytics

    try:
        from executorch import version as executorch_version

        et_version = executorch_version.__version__
    except Exception:
        et_version = "unknown"

    return {
        "built_at": datetime.now(timezone.utc).isoformat(),
        "torch_version": str(torch.__version__),
        "executorch_version": str(et_version),
        "ultralytics_version": str(ultralytics.__version__),
        "input_shape": [1, 3, imgsz, imgsz],
        "input_layout": "NCHW",
        "input_dtype": "float32",
    }
