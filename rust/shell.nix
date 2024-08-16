let
  rust-overlay-source = builtins.fetchGit {
    url = "https://github.com/oxalica/rust-overlay";
  };

  rust-overlay = import rust-overlay-source;

  pkgs = import <nixpkgs> {
    overlays = [ rust-overlay ];
  };

in
  # Make a shell with the dependencies we need
  pkgs.mkShell {
    packages = with pkgs; [
      cargo
      cargo-watch
      cargo-nextest
      cargo-expand
      cargo-flamegraph
      rust-analyzer
      evcxr
    ];

  }
