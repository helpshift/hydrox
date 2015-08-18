(ns hydrox.analyse.test-test
  (:use midje.sweet)
  (:require [hydrox.analyse.test :refer :all]))

^{:refer hydrox.analyse.test/find-frameworks :added "0.1"}
(fact "finds the corresponding test framework"

  (find-frameworks '(ns ...
                      (:use midje.sweet)))
  => #{:midje})
