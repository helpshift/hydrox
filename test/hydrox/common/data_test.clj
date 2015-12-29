(ns hydrox.common.data-test
  (:use midje.sweet)
  (:require [hydrox.common.data :refer :all]))

^{:refer hydrox.common.data/folio :added "0.1"}
(fact "constructs a folio object")

^{:refer hydrox.common.data/references :added "0.1"}
(fact "constructs a reference object")
