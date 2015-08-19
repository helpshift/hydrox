(ns hydrox.analyse.test-test
  (:use midje.sweet)
  (:require [hydrox.analyse.test :refer :all]))

^{:refer hydrox.analyse.test/find-frameworks :added "0.1"}
(fact "find test frameworks given a namespace form"
  (find-frameworks '(ns ...
                      (:use midje.sweet)))
  => #{:midje}

  (find-frameworks '(ns ...
                      (:use clojure.test)))
  => #{:clojure})
