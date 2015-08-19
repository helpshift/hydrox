(ns hydrox.analyse.test-test
  (:use midje.sweet)
  (:require [hydrox.analyse.test :refer :all]
            [hydrox.analyse.test.common :as common]))

^{:refer hydrox.analyse.test/find-frameworks :added "0.1"}
(fact "find test frameworks given a namespace form"
  (find-frameworks '(ns ...
                      (:use midje.sweet)))
  => #{:midje}

  (find-frameworks '(ns ...
                      (:use clojure.test)))
  => #{:clojure})

^{:refer hydrox.analyse.test/analyse-test-file :added "0.1"}
(fact "analyses a test file for docstring forms"
  (-> (analyse-test-file "example/test/example/core_test.clj" {})
      (update-in '[example.core foo :docs] common/join-nodes))
  => '{example.core {foo {:docs "1\n  => 1", :meta {:added "0.1"}}}})
