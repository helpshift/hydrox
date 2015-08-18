(ns hydrox.analyse.test.common-test
  (:use midje.sweet)
  (:require [hydrox.analyse.test.common :refer :all]
            [rewrite-clj.zip :as z]))

^{:refer hydrox.analyse.test.common/gather-meta :added "0.1"}
(fact "gets the metadata for a particular form"
  (-> (z/of-string "^{:refer clojure.core/+ :added \"0.1\"}\n(fact ...)")
      z/down z/right z/down
      gather-meta)
  => '{:added "0.1", :ns clojure.core, :var +, :refer clojure.core/+})

^{:refer hydrox.analyse.test.common/gather-string :added "0.1"}
(fact "creates correctly spaced code string from normal docstring"
  
  (-> (z/of-string "\"hello\nworld\nalready\"")
      (gather-string)
      (str))
  => "\"hello\n  world\n  already\"")

^{:refer hydrox.analyse.test.common/strip-quotes :added "0.1"}
(fact "takes away the quotes from a string for formatting purposes"

  (strip-quotes "\"hello\"")
  => "hello")


