(ns nitrox.analyse.test-test
  (:use midje.sweet)
  (:require [nitrox.analyse.test :refer :all]))

^{:refer nitrox.analyse.test/find-frameworks :added "0.1"}
(fact "finds the corresponding test framework"

  (find-frameworks '(ns ...
                      (:use midje.sweet)))
  => #{:midje})
