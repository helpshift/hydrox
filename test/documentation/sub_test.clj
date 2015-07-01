(ns documentation.sub-test
  (:use midje.sweet)
  (:require [nitrox.analyser.test]))

(comment
  (hello there))

[[:ns {}]]

[[:reference {:refer nitrox.analyser.test/find-frameworks :mode :source}]]
