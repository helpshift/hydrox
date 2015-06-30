(ns documentation.example-test
  (:use midje.sweet))

[[{:caption "STUFF"}]]

"hello there"

[[:chapter {:title "Love and Truth"}]]

[[:file {:src "test/documentation/sub_test.clj"}]]

[[{:caption "CODING"}]]

"oeuoeuoeuoe"

[[{:caption "SOMETHING GOOD"}]]

(+ 1 1)
(+ 1 1)
(+ 1 1)

[[:ns {:caption "current namespace"}]]

(facts "hello world"
  (+ 1 1) => 1
  
  "Give me some code"
  
  (+ 2 3) 
  => 5)

[[:ns {:caption "" :ns clojure.core}]]
  