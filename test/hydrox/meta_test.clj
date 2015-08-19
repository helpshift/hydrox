(ns hydrox.meta-test
  (:use midje.sweet)
  (:require [hydrox.meta :refer :all]))

^{:refer hydrox.meta/selector :added "0.1"}
(fact "builds a selector for functions"
  
  (selector 'hello)
  => '[(#{defn defmulti} | hello ^:%?- string? ^:%?- map? & _)])

^{:refer hydrox.meta/edit-file :added "0.1"}
(fact "helper function for file manipulation used by import and purge")
