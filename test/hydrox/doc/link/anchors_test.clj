(ns hydrox.doc.link.anchors-test
  (:use midje.sweet)
  (:require [hydrox.doc.link.anchors :refer :all]))

^{:refer hydrox.doc.link.anchors/link-anchors-lu :added "0.1"}
(fact "creates the anchor lookup for tags and numbers"
  (-> {:articles {"example" {:elements [{:type :chapter :tag "hello" :number "1"}]}}}
      (link-anchors-lu "example")
      :anchors-lu)
  => {"example" {:by-number {:chapter {"1" {:type :chapter, :tag "hello", :number "1"}}},
                 :by-tag {"hello" {:type :chapter, :tag "hello", :number "1"}}}})

^{:refer hydrox.doc.link.anchors/link-anchors :added "0.1"}
(fact "creates a global anchors list based on the lookup"

  (-> {:articles {"example" {:elements [{:type :chapter :tag "hello" :number "1"}]}}}
      (link-anchors-lu "example")
      (link-anchors "example")
      :anchors)
  => {"example" {"hello" {:type :chapter, :tag "hello", :number "1"}}})
