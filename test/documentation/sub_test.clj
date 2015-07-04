(ns documentation.sub-test
  (:use midje.sweet)
  (:require [nitrox.analyse.test]))

(comment
  (hello there))

[[:ns {}]]

[[:reference {:refer nitrox.analyse.test/find-frameworks :mode :source}]]
