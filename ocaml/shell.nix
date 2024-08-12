{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    ocaml
    opam
    ocamlPackages.ocamlformat
    ocamlPackages.dune_3
    ocamlPackages.merlin
    ocamlPackages.ocp-indent
    ocamlPackages.utop
    ocamlPackages.ocaml-lsp
    ocamlPackages.ounit2
  ];

  shellHook = ''
    echo "OCaml environment loaded"
    export OPAMROOT="$PWD/.opam"
    export OPAMYES=1
    if [ ! -d "$OPAMROOT" ]; then
      opam init --disable-sandboxing --bare
      eval $(opam env)
      opam install . --deps-only
    else
      eval $(opam env)
    fi
  '';
}
