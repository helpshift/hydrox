(ns example.core-test
  (:use clojure.test)
  (:require [example.core :refer :all]))

^{:refer example.core/foo :added "0.1"}
(deftest foo-test
  (is (= 1 1)))
