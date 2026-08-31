# Python environment and wheels for ExecuTorch model export (Ultralytics + Vulkan/XNNPACK).
{
  pkgs,
}:
let
  inherit (pkgs) lib;

  python = pkgs.python312;
  torchVersion = "2.13.0";

  # Official CPU wheel. nixpkgs torch is not 2.13.
  torch = python.pkgs.buildPythonPackage {
    pname = "torch";
    version = "${torchVersion}+cpu";
    format = "wheel";
    src = pkgs.fetchurl {
      url = "https://download.pytorch.org/whl/cpu/torch-${torchVersion}%2Bcpu-cp312-cp312-manylinux_2_28_x86_64.whl";
      hash = "sha256-TKSpOUsMdxI4pPc1kP27xN662F7Q+mPQJq4bCF2n1uI=";
    };
    nativeBuildInputs = [ pkgs.autoPatchelfHook ];
    buildInputs = [
      pkgs.stdenv.cc.cc.lib
      pkgs.zlib
      pkgs.llvmPackages.openmp
    ];
    autoPatchelfIgnoreMissingDeps = true;
    propagatedBuildInputs = with python.pkgs; [
      filelock
      fsspec
      jinja2
      networkx
      setuptools
      sympy
      typing-extensions
    ];
    pythonImportsCheck = [ ];
    doCheck = false;
  };

  mkWheel =
    {
      pname,
      version,
      url,
      hash,
      format ? "wheel",
      propagatedBuildInputs ? [ ],
      extraAttrs ? { },
    }:
    python.pkgs.buildPythonPackage (
      {
        inherit
          pname
          version
          format
          propagatedBuildInputs
          ;
        src = pkgs.fetchurl { inherit url hash; };
        pipInstallFlags = [ "--no-deps" ];
        doCheck = false;
        pythonImportsCheck = [ ];
        dontCheckRuntimeDeps = true;
      }
      // extraAttrs
    );

  torchvision = mkWheel {
    pname = "torchvision";
    version = "0.28.0";
    url = "https://download.pytorch.org/whl/cpu/torchvision-0.28.0%2Bcpu-cp312-cp312-manylinux_2_28_x86_64.whl";
    hash = "sha256-tUXUb00vnTA4EoHPIodL/h0yqKew7oOW/M3onzDGqdk=";
    propagatedBuildInputs = [
      torch
      python.pkgs.pillow
      python.pkgs.numpy
    ];
    extraAttrs = {
      nativeBuildInputs = [ pkgs.autoPatchelfHook ];
      buildInputs = [
        pkgs.stdenv.cc.cc.lib
        pkgs.zlib
        pkgs.libpng
        pkgs.libjpeg
      ];
      autoPatchelfIgnoreMissingDeps = true;
    };
  };

  ultralytics-thop = mkWheel {
    pname = "ultralytics-thop";
    version = "2.1.6";
    url = "https://files.pythonhosted.org/packages/53/98/f1fa3d40d548c8a2a3eec33b7f856063bb6c7d51e16d5198b1f390b2c79d/ultralytics_thop-2.1.6-py3-none-any.whl";
    hash = "sha256-I/e4rRJPo0MsGn3pJ5ECxP3aaZIWAyp9/0n4fsPRo68=";
    propagatedBuildInputs = [
      torch
      python.pkgs.numpy
    ];
  };

  nvidia-ml-py = mkWheel {
    pname = "nvidia-ml-py";
    version = "13.610.43";
    url = "https://files.pythonhosted.org/packages/23/45/caa600acfab94560807a20a64b5830d2cd3c3202b7f1328644d70b7d6bd8/nvidia_ml_py-13.610.43-py3-none-any.whl";
    hash = "sha256-8TxyaY7e9JL5hcwiXxT6r+aK4GWi5Af0W99vS5tD/eg=";
  };

  ultralytics-platform = mkWheel {
    pname = "ultralytics-platform";
    version = "0.1.18";
    url = "https://files.pythonhosted.org/packages/a0/4b/b3de98da370a15efba580848050c32605e5354436dfe49d077444aa03604/ultralytics_platform-0.1.18-py3-none-any.whl";
    hash = "sha256-DmcnqsemcufJffhO5n0M4PopEF49jcBdjoTXu2a0Hfk=";
    propagatedBuildInputs = [ python.pkgs.httpx ];
  };

  ultralytics = mkWheel {
    pname = "ultralytics";
    version = "8.4.128";
    url = "https://files.pythonhosted.org/packages/67/72/0a664f5ef2b3f4dd902a8305d9d89ca81965968c181187ddf9685c61174c/ultralytics-8.4.128-py3-none-any.whl";
    hash = "sha256-jDxKlom3o/RdgaZDburVZAgNJn6Vtp/yUE3na1e/LO0=";
    propagatedBuildInputs = [
      torch
      torchvision
      ultralytics-thop
      nvidia-ml-py
      ultralytics-platform
      python.pkgs.filelock
      python.pkgs.numpy
      python.pkgs.matplotlib
      python.pkgs.opencv4
      python.pkgs.pillow
      python.pkgs.pyyaml
      python.pkgs.requests
      python.pkgs.psutil
      python.pkgs.polars
    ];
  };

  torchao = mkWheel {
    pname = "torchao";
    version = "0.18.0";
    url = "https://files.pythonhosted.org/packages/19/55/ed9ad98f0f09d5a1124d09830043d13a39e63539f9590d2bdb6d71cbc4a4/torchao-0.18.0-cp310-abi3-manylinux_2_24_x86_64.manylinux_2_28_x86_64.whl";
    hash = "sha256-ZUCxSOQLqBy9TehjkiJaB2oVkRRunOuwmbOyNLqf7r4=";
    propagatedBuildInputs = [ torch ];
    extraAttrs = {
      nativeBuildInputs = [ pkgs.autoPatchelfHook ];
      buildInputs = [ pkgs.stdenv.cc.cc.lib ];
      autoPatchelfIgnoreMissingDeps = true;
    };
  };

  # nixpkgs pytorch-tokenizers (1.2) pulls transformers → safetensors → torch 2.11 from source.
  pytorch-tokenizers = mkWheel {
    pname = "pytorch-tokenizers";
    version = "1.4.1";
    url = "https://files.pythonhosted.org/packages/ad/b3/4d38b4e5cd951df08511e0a11637baad56dec6ca82bca7b594c8d0c85e59/pytorch_tokenizers-1.4.1-cp312-cp312-manylinux_2_28_x86_64.whl";
    hash = "sha256-P+sz4E3Kos6m3L6Zq6Itoq2B0e0wW8CvUqyrWz+P8JQ=";
    propagatedBuildInputs = with python.pkgs; [
      sentencepiece
      tiktoken
      tokenizers
    ];
  };

  executorch = mkWheel {
    pname = "executorch";
    version = "1.4.0";
    url = "https://files.pythonhosted.org/packages/1c/55/afb80f13ffd785b1235718c74d2fda17748b53bc111b4c3168d9c2055ca6/executorch-1.4.0-cp312-cp312-manylinux_2_28_x86_64.whl";
    hash = "sha256-+xPr+Pq1tcfvWG+ns0fs04vo/UJoRUUZOkleyyEoe1U=";
    extraAttrs = {
      nativeBuildInputs = [ pkgs.autoPatchelfHook ];
      buildInputs = [
        pkgs.stdenv.cc.cc.lib
        pkgs.zlib
        pkgs.llvmPackages.openmp
      ];
      autoPatchelfIgnoreMissingDeps = true;
      propagatedBuildInputs = [
        torch
        python.pkgs.numpy
        python.pkgs.pyyaml
        python.pkgs.packaging
        python.pkgs.sympy
        python.pkgs.typing-extensions
        python.pkgs.ruamel-yaml
        python.pkgs.flatbuffers
        python.pkgs.tabulate
        python.pkgs.mpmath
        pytorch-tokenizers
        torchao
      ];
    };
  };

  exportPython = python.withPackages (ps: [
    ps.pyyaml
    torch
    torchvision
    ultralytics
    executorch
  ]);

  glLibs = lib.makeLibraryPath [
    pkgs.libGL
    pkgs.libglvnd
    pkgs.glib
    pkgs.zlib
    pkgs.fontconfig
    pkgs.freetype
    pkgs.libpng
    pkgs.stdenv.cc.cc.lib
  ];

  exportDir = ./export;

  exportEnv = {
    LD_LIBRARY_PATH = glLibs;
    MPLBACKEND = "Agg";
    YOLO_VERBOSE = "False";
    HF_HUB_OFFLINE = "1";
    TRANSFORMERS_OFFLINE = "1";
    PYTHONPATH = exportDir;
  };
in
{
  inherit
    exportPython
    exportEnv
    exportDir
    ;
}
