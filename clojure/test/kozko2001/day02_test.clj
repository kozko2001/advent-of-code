(ns kozko2001.day02-test
  (:require [clojure.test :refer :all]
            [kozko2001.day02 :refer :all]))

(def test-data-part1 (slurp "resources/day02-test-part1.txt"))
(def data-part1 (slurp "resources/day02-part1.txt"))

(deftest part1-puzzle
  (is (= 8 (part1 test-data-part1))))

(deftest part1-real
  (is (= 2776 (part1 data-part1))))

(deftest part2-puzzle
  (is (= 2286 (part2 test-data-part1))))

(deftest part2-real
  (is (= 68638 (part2 data-part1))))
(clojure.test/run-tests)
