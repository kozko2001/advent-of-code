
open OUnit2

let test_add _ =
  assert_equal 4 (Ocaml.Day02.add 2 2);
  assert_equal 0 (Ocaml.Day02.add (-2) 2)

let suite =
  "Test Suite" >::: [
    "test_add" >:: test_add;
  ]

let () =
  run_test_tt_main suite
