{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  # Specify the packages you want in your environment
  buildInputs = [
    pkgs.openjdk # Java, required for running Clojure
    pkgs.clojure # Clojure CLI tools
    pkgs.clojure-lsp # Clojure LSP for IDE support
    pkgs.babashka # Optional, but useful for scripting with Clojure
  ];

  # Optional: Set environment variables, like for Java options
  shellHook = ''
    export JAVA_OPTS="-Xmx2g"
  '';
}
