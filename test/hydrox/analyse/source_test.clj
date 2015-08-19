(ns hydrox.analyse.source-test
  (:use midje.sweet)
  (:require [hydrox.analyse.source :refer :all]))

^{:refer hydrox.analyse.source/analyse-source-file :added "0.1"}
(fact "analyses a source file for indentation")
