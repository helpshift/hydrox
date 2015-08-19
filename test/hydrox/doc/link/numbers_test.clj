(ns hydrox.doc.link.numbers-test
  (:use midje.sweet)
  (:require [hydrox.doc.link.numbers :refer :all]))

^{:refer hydrox.doc.link.numbers/increment :added "0.1"}
(fact "increments a string for alphanumerics and numbers"
  (increment "1")
  => "2"
  
  (increment "A")
  => "B")

^{:refer hydrox.doc.link.numbers/link-numbers-loop :added "0.1"}
(fact "main loop logic for generation of numbers")

^{:refer hydrox.doc.link.numbers/link-numbers :added "0.1"}
(fact "creates numbers for main sections, images, code and equations"
  (link-numbers {:articles {"example" {:elements [{:type :chapter :title "hello"}
                                                  {:type :section :title "world"}]}}}
                "example")
  {:articles {"example"
              {:elements [{:type :chapter, :title "hello", :number "1"}
                          {:type :section, :title "world", :number "1.1"}]}}})
