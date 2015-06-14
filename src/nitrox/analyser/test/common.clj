(ns nitrox.analyser.test.common)

(defmulti analyse-test-file (fn [type file] type))