(ns kozko2001.day02
  (:require [instaparse.core :as insta])
  (:gen-class))

(defrecord Game [id sets])
(defrecord Set [red green blue])

(def game-grammar
  "<game> = game-sequence
   <game-sequence> = (game-statement <newline?>)*
   game-statement = <'Game'> <space> number <':'> <space> set-sequence
   set-sequence = (<space?> set-statement <';'?>)*
   set-statement = color-count (<','> <space> color-count)*
   color-count = number <space> color
   color = 'red' | 'green' | 'blue'
   number = #'\\d+'
   space = #'\\s+'
   <newline> = #'\\r?\\n'")
(def parser (insta/parser game-grammar))

(defn parse-number [s]
  (Integer/parseInt s))

(defn transform-color-count [[_ [_ num] [_ color]]]
  {(keyword color) (parse-number num)})

(defn transform-set-statement [[_ & set-statement]]
  (reduce (fn [acc color-count]
            (merge-with + acc (transform-color-count color-count)))
          {:red 0 :green 0 :blue 0}
          set-statement))

(defn transform-game-statement [[_ [_ num] [_ & sets]]]
  (->Game (parse-number num)
          (map transform-set-statement sets)))

(defn transform [parsed-data]
  (map transform-game-statement parsed-data))

(def bag {:red 12 :green 13 :blue 14})

(defn impossible-draw [draws bag]
  (every? (fn [g] (every? #(<= (% g) (% bag))  [:green :blue :red])) draws))

(defn part1 [input]
  (->> (parser input)
       transform
       (filter #(impossible-draw (:sets %) bag))
       (map :id)
       (reduce +)))

(defn minimum-possible-bag [draws]
  (apply merge-with max draws))

(defn power-cubes [bag]
  (apply * (vals bag)))

(defn part2 [input]
  (->> (parser input)
       transform
       (map #(minimum-possible-bag (:sets %)))
       (map power-cubes)
       (reduce +)))
