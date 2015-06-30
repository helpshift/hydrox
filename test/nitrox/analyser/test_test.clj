(ns nitrox.analyser.test-test
  (:use midje.sweet)
  (:require [nitrox.analyser.test :refer :all]))

^{:refer nitrox.analyser.test/find-frameworks :added "0.1"}
(fact "finds the corresponding test framework"

  (find-frameworks '(ns ...
                      (:use midje.sweet)))
  => #{:midje})
