#!/usr/bin/env python3
"""Export YOLO26 models to ExecuTorch .pte files for XNNPACK and Vulkan."""

from __future__ import annotations

import os
import shutil
import sys
import tempfile
from pathlib import Path
from typing import Any

import torch
import yaml
from ultralytics import YOLO

from executorch_export import (
    build_export_metadata,
    download_file,
    export_ultralytics_executorch,
    sha256_file,
    smoke_test_pte,
)

MANIFEST_ENV = "MANIFEST_PATH"
OUTPUT_ENV = "OUTPUT_DIR"
GENERATED_MANIFEST_NAME = "manifest.generated.yaml"


def manifest_path_from_env() -> Path:
    raw = os.environ.get(MANIFEST_ENV)
    if not raw:
        raise RuntimeError(f"{MANIFEST_ENV} is not set")
    path = Path(raw).resolve()
    if not path.is_file():
        raise FileNotFoundError(f"Manifest not found: {path}")
    return path


def output_dir_from_env() -> Path:
    raw = os.environ.get(OUTPUT_ENV)
    if not raw:
        raise RuntimeError(f"{OUTPUT_ENV} is not set")
    return Path(raw).resolve()


def load_manifest(manifest_path: Path) -> dict[str, Any]:
    with manifest_path.open("r", encoding="utf-8") as f:
        return yaml.safe_load(f) or {}


def model_name(manifest: dict[str, Any]) -> str:
    return str(manifest.get("model") or "yolo26")


def resolve_weights(manifest: dict[str, Any], output_dir: Path) -> Path:
    weights = manifest.get("weights") or {}
    local = weights.get("path")
    if local:
        path = Path(local).expanduser()
        if not path.is_file():
            raise FileNotFoundError(f"weights.path not found: {path}")
        print(f"Using weights.path={path}")
        return path.resolve()

    url = weights.get("url")
    if not url:
        raise RuntimeError("No weights configured. Set weights.path or weights.url in the manifest.")

    filename = weights.get("filename") or Path(url).name or f"{model_name(manifest)}.pt"
    dest = output_dir / "weights" / filename
    if dest.is_file():
        actual = sha256_file(dest)
        expected = (weights.get("sha256") or "").lower()
        if not expected or actual == expected:
            print(f"Reusing cached weights at {dest}")
            return dest

    download_file(url, dest, expected_sha256=weights.get("sha256"))
    return dest


def export_yolo26(weights_path: Path, imgsz: int, work_dir: Path) -> tuple[Path, Path]:
    local_weights = work_dir / weights_path.name
    shutil.copy2(weights_path, local_weights)
    yolo = YOLO(str(local_weights))
    return export_ultralytics_executorch(yolo, imgsz=imgsz, work_dir=work_dir)


def write_manifest(
    generated_path: Path,
    manifest: dict[str, Any],
    imgsz: int,
    xnnpack_pte: Path,
    vulkan_pte: Path,
    weights_path: Path,
) -> None:
    manifest["imgsz"] = imgsz
    manifest["weights"] = manifest.get("weights") or {}
    manifest["weights"]["resolved_path"] = str(weights_path)
    manifest["weights"]["sha256"] = sha256_file(weights_path)
    manifest["weights"].setdefault("url", None)
    manifest["weights"].setdefault("filename", weights_path.name)
    manifest["weights"].setdefault("source", "override")

    artifacts = manifest.setdefault("artifacts", {})
    xnnpack = artifacts.setdefault("xnnpack", {})
    vulkan = artifacts.setdefault("vulkan", {})
    xnnpack["path"] = "xnnpack/model.pte"
    xnnpack["sha256"] = sha256_file(xnnpack_pte)
    xnnpack["bytes"] = xnnpack_pte.stat().st_size
    vulkan["path"] = "vulkan/model.pte"
    vulkan["sha256"] = sha256_file(vulkan_pte)
    vulkan["bytes"] = vulkan_pte.stat().st_size

    manifest["export"] = build_export_metadata(imgsz=imgsz)

    generated_path.parent.mkdir(parents=True, exist_ok=True)
    tmp_manifest = generated_path.with_suffix(".yaml.tmp")
    with tmp_manifest.open("w", encoding="utf-8") as f:
        yaml.safe_dump(manifest, f, sort_keys=False, default_flow_style=False)
    tmp_manifest.replace(generated_path)


def main() -> int:
    manifest_path = manifest_path_from_env()
    output_dir = output_dir_from_env()
    manifest = load_manifest(manifest_path)
    name = model_name(manifest)
    imgsz = int(manifest.get("imgsz", 640))
    if imgsz % 32 != 0:
        print(f"imgsz must be a multiple of 32, got {imgsz}", file=sys.stderr)
        return 1

    output_dir.mkdir(parents=True, exist_ok=True)
    shutil.copy2(manifest_path, output_dir / "manifest.yaml")
    weights_path = resolve_weights(manifest, output_dir)

    with tempfile.TemporaryDirectory(prefix=f"{name}-export-") as tmp:
        work_dir = Path(tmp)
        print(f"Exporting {name} at imgsz={imgsz} ...")
        xnnpack_pte, vulkan_pte = export_yolo26(weights_path, imgsz, work_dir)

        if os.environ.get("SMOKE_TEST", "1") != "0":
            example = torch.zeros(1, 3, imgsz, imgsz, dtype=torch.float32)
            smoke_test_pte(xnnpack_pte, example)
            print("Skipping Vulkan smoke test (no GPU in export container).")

        xnnpack_out = output_dir / "xnnpack"
        vulkan_out = output_dir / "vulkan"
        xnnpack_out.mkdir(parents=True, exist_ok=True)
        vulkan_out.mkdir(parents=True, exist_ok=True)

        shutil.copy2(xnnpack_pte, xnnpack_out / "model.pte")
        shutil.copy2(vulkan_pte, vulkan_out / "model.pte")
        xnnpack_meta = work_dir / "xnnpack" / "metadata.yaml"
        vulkan_meta = work_dir / "vulkan" / "metadata.yaml"
        if xnnpack_meta.is_file():
            shutil.copy2(xnnpack_meta, xnnpack_out / "metadata.yaml")
        if vulkan_meta.is_file():
            shutil.copy2(vulkan_meta, vulkan_out / "metadata.yaml")

    write_manifest(
        output_dir / GENERATED_MANIFEST_NAME,
        manifest,
        imgsz,
        output_dir / "xnnpack" / "model.pte",
        output_dir / "vulkan" / "model.pte",
        weights_path,
    )
    print(f"Export complete -> {output_dir}")
    print(f"Generated manifest -> {output_dir / GENERATED_MANIFEST_NAME}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
