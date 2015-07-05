(ns documentation.frameworks.fact
  (:use midje.sweet))

^{:refer clojure.core/swap! :added "0.5"}
(fact "We do stuff"
  (str "there") => "there")
