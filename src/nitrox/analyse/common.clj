(ns nitrox.analyse.common)

(defmulti analyse-file (fn [type file & [opts]] type))
