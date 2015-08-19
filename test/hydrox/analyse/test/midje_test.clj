(ns hydrox.analyse.test.midje-test
  (:use midje.sweet)
  (:require [hydrox.analyse.test.midje :refer :all]
            [hydrox.analyse.test.common :as common]
            [rewrite-clj.zip :as z]))

^{:refer hydrox.analyse.test.midje/gather-fact :added "0.1"}
(fact "Make docstring notation out of fact form"
  (-> "^{:refer example/hello-world :added \"0.1\"}
       (fact \"Sample test program\"\n  (+ 1 1) => 2\n  (long? 3) => true)"
      (z/of-string)
      z/down z/right z/down z/right
      (gather-fact)
      :docs
      common/join-nodes)
  => "\"Sample test program\"\n  (+ 1 1) => 2\n  (long? 3) => true")
