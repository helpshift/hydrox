(ns documentation.sample-document
  (:require [midje.sweet :refer :all]
            [hydrox.core :as hydrox]))

[[:chapter {:tag "hello" :title "Introduction"}]]

"This is an introduction to the exciting library"

[[:section {:title "Defining a function"}]]

"We define function `add-5`"

[[{:numbered false}]]
(defn add-5 [x]
  (+ x 5))

[[:section {:title "Testing a function"}]]

"`add-5` outputs the following results seen in
 [e.{{add-5-1}}](#add-5-1) and [e.{{add-5-10}}](#add-5-10):"

[[{:tag "eq1"}]]
(fact
  (+ 1 0) => 1
  (+ 1 3) => 4)

;;[[{:tag "eq2" :title "some equations 2"}]]
(comment
  (+ 1 1) => 2
  (+ 1 3) => 2)

(facts
  [[{:tag "add-5-1" :title "1 add 5 equals 6"}]]
  (add-5 1) => 6

  [[{:tag "add-5-10" :title "10 add 5 equals 15"}]]
  (add-5 10) => 15)

[[:chapter {:title "Walkthrough"}]]

"Here is a walkthrough for the library"

[[:chapter {:title "API Reference"}]]

"The API reference here:"
