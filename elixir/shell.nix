{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = [
    pkgs.elixir
    pkgs.erlang
    pkgs.elixir-ls
  ];

  shellHook = ''
    echo "Welcome to the Elixir Advent of Code environment!"
    export MIX_ENV=dev
    mix deps.get
  '';
}
