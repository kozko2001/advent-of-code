# aoc-clojure

## Running tests

- run all test -> `clojure -M:test`
- run specific day -> `clojure -M:test --namespace kozko2001.day02-test`

## Neovim - conjurer

execute `clj -M:repl/conjure` to start the nrepl

- `<leader>eb` -> evaluate the current buffer
- `<leader>ee` -> evaluate the current inner form
- `<leader>er` -> evaluate the current outter most node
- `<leader>E`  -> evaluate selected in visual mode
- `<leader>ew` -> inspect content of variable

to execute specific test in conjurer:

evaluate the tests with `<leader>eb`

execute `:ConjureEval (clojure.test/run-tests)` 
