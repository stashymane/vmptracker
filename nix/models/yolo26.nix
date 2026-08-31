# YOLO26 → ExecuTorch .pte export (XNNPACK + Vulkan).
{
  pkgs,
  executorchExport,
}:
let
  inherit (pkgs) lib;

  mkYolo26Export =
    {
      manifestYaml,
      pname ? null,
    }:
    let
      manifestText = builtins.readFile manifestYaml;

      readManifestField = section: key:
        let
          hits = lib.filter (
            line:
            lib.hasInfix "${key}:" line
            && !(lib.hasPrefix "#" (lib.trim line))
          ) (lib.splitString "\n" section);
        in
        if hits == [ ] then
          null
        else
          lib.trim (lib.elemAt (lib.splitString "${key}:" (lib.head hits)) 1);

      modelName =
        let val = readManifestField manifestText "model";
        in if val != null then val else lib.removeSuffix "-manifest.yaml" (builtins.baseNameOf (toString manifestYaml));

      exportPname = if pname != null then pname else "${modelName}-export";

      weightsSection =
        let
          afterWeights = lib.elemAt (lib.splitString "weights:" manifestText) 1;
        in
        lib.head (lib.splitString "\nartifacts:" afterWeights);

      weightsSpec = {
        url = readManifestField weightsSection "url";
        filename = readManifestField weightsSection "filename";
        nix_hash = readManifestField weightsSection "nix_hash";
      };

      exportSrc = pkgs.linkFarm "${exportPname}-src" [
        { name = "manifest.yaml"; path = manifestYaml; }
        { name = "yolo26_export.py"; path = ../export/yolo26_export.py; }
        { name = "executorch_export.py"; path = ../export/executorch_export.py; }
      ];

      weights = pkgs.fetchurl {
        name = if weightsSpec.filename != null then weightsSpec.filename else "${modelName}.pt";
        url = weightsSpec.url;
        hash = weightsSpec.nix_hash;
      };

      asset = pkgs.stdenv.mkDerivation {
        pname = exportPname;
        version = builtins.substring 0 12 (builtins.hashFile "sha256" manifestYaml);
        src = exportSrc;
        nativeBuildInputs = [ executorchExport.exportPython ];
        inherit (executorchExport.exportEnv)
          LD_LIBRARY_PATH
          MPLBACKEND
          YOLO_VERBOSE
          HF_HUB_OFFLINE
          TRANSFORMERS_OFFLINE
          ;
        SMOKE_TEST = "1";
        buildPhase = ''
          runHook preBuild
          export HOME="$TMPDIR"
          export OUTPUT_DIR="$out"
          export PYTHONPATH="$src"

          work="$TMPDIR/${modelName}"
          mkdir -p "$work"
          manifest="$work/manifest.yaml"
          export MANIFEST_SRC="$src/manifest.yaml"
          export MANIFEST_EDIT="$manifest"
          export WEIGHTS_PATH="${weights}"
          ${executorchExport.exportPython}/bin/python -c '
import os
import yaml
from pathlib import Path

data = yaml.safe_load(Path(os.environ["MANIFEST_SRC"]).read_text())
data.setdefault("weights", {})["path"] = os.environ["WEIGHTS_PATH"]
Path(os.environ["MANIFEST_EDIT"]).write_text(yaml.safe_dump(data, sort_keys=False))
'
          export MANIFEST_PATH="$manifest"
          mkdir -p "$out"
          ${executorchExport.exportPython}/bin/python "$src/yolo26_export.py"
          runHook postBuild
        '';
        installPhase = "true";
        dontFixup = true;
      };
    in
    {
      inherit asset modelName;
    };
in
{
  inherit mkYolo26Export;
}
