{
  description = "vmptracker nix flake";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-26.05";
  };

  outputs =
    { nixpkgs, ... }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
      lib = pkgs.lib;

      executorchExport = import ./nix/executorch-export.nix { inherit pkgs; };
      yolo26 = import ./nix/models/yolo26.nix {
        inherit pkgs executorchExport;
      };

      # Add new models here: name = path to manifest YAML.
      modelManifests = {
        yolo26n-face = ./nix/models/yolo26n-face-manifest.yaml;
      };

      models = lib.mapAttrs (
        _name: manifestYaml:
        yolo26.mkYolo26Export {
          inherit manifestYaml;
        }
      ) modelManifests;

      modelPackages = lib.mapAttrs (_name: model: model.asset) models;

      allModels = pkgs.runCommand "all-models" { } ''
        mkdir -p $out
        ${lib.concatMapStrings (name: ''
          mkdir -p $out/${name}
          cp -r ${models.${name}.asset}/* $out/${name}/
        '') (builtins.attrNames modelManifests)}
      '';
    in
    {
      packages.${system} = modelPackages // {
        default = models.yolo26n-face.asset;
        all-models = allModels;
      };

      devShells.${system}.default = pkgs.mkShell {
        name = "vmptracker-models";
        packages = [ executorchExport.exportPython ];
        inherit (executorchExport.exportEnv)
          LD_LIBRARY_PATH
          MPLBACKEND
          YOLO_VERBOSE
          HF_HUB_OFFLINE
          TRANSFORMERS_OFFLINE
          PYTHONPATH
          ;
      };
    };
}
