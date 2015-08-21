(ns hydrox.doc.checks-test
  (:use midje.sweet)
  (:require [hydrox.doc.checks :refer :all]
            [rewrite-clj.zip :as z]))

^{:refer hydrox.doc.checks/directive? :added "0.1"}
(fact "checks if the element is a directive."

  (-> "[[:chapter {:title \"A Story\"}]]"
      z/of-string
      directive?)
  => true)

^{:refer hydrox.doc.checks/attribute? :added "0.1"}
(fact "checks if the element is an attribute."

  (-> "[[{:title \"A Story\"}]]"
      z/of-string
      attribute?)
  => true)

^{:refer hydrox.doc.checks/code-directive? :added "0.1"}
(fact "checks if the element is a code directive"

  (-> "[[:code {:type :javascript} 
         \"1 + 1 == 2\"]]"
      z/of-string
      code-directive?)
  => true)

^{:refer hydrox.doc.checks/ns? :added "0.1"}
(fact "checks if the element is a ns form"

  (-> "(ns ...)"
      z/of-string
      ns?)
  => true)

^{:refer hydrox.doc.checks/fact? :added "0.1"}
(fact "checks if the element is a fact form"

  (-> "(fact ...)"
      z/of-string
      fact?)
  => true)

^{:refer hydrox.doc.checks/facts? :added "0.1"}
(fact "checks if the element is a facts form"

  (-> "(facts ...)"
      z/of-string
      facts?)
  => true)

^{:refer hydrox.doc.checks/comment? :added "0.1"}
(fact "checks if the element is a comment form")

^{:refer hydrox.doc.checks/paragraph? :added "0.1"}
(fact "checks if the element is a paragraph (string)")

^{:refer hydrox.doc.checks/whitespace? :added "0.1"}
(fact "checks if the element is a whitespace element")
