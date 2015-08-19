(ns hydrox.doc.link.namespaces-test
  (:use midje.sweet)
  (:require [hydrox.doc.link.namespaces :refer :all]))

^{:refer hydrox.doc.link.namespaces/link-namespaces :added "0.1"}
(fact "link elements with `:ns` forms to code"

  (link-namespaces
   {:articles {"example" {:elements [{:type :ns :ns "clojure.core"}]}}
    :namespaces {"clojure.core" {:code "(ns clojure.core)"}}}
   "example")
  => {:articles {"example" {:elements [{:type :code
                                        :ns "clojure.core"
                                        :origin :ns
                                        :indentation 0
                                        :code "(ns clojure.core)"}]}}
      :namespaces {"clojure.core" {:code "(ns clojure.core)"}}})
