(ns documentation.sub-test
  (:use midje.sweet)
  (:require [hydrox.analyse.test]))

(comment
  (hello there))

[[:ns {}]]

[[:reference {:refer hydrox.analyse.test/find-frameworks :mode :source}]]
