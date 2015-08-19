(ns hydrox.doc.link.stencil-test
  (:use midje.sweet)
  (:require [hydrox.doc.link.stencil :refer :all]))

^{:refer hydrox.doc.link.stencil/transform-stencil :added "0.1"}
(fact "takes a short-form and expands using anchor information"
  (transform-stencil "{{hello}}" "example"
                     {"example" {"hello" {:number 1}}})
  => "1"

  (transform-stencil "{{stuff/create}}" "example"
                     {"stuff" {"create" {:number 2}}})
  => "2"

  (transform-stencil "[[stuff/create]]" "example"
                     {"stuff" {"create" {:number 2}}})
  => "[2](stuff.html#create)"

  (transform-stencil "[[create]]" "example"
                     {"example" {"create" {:number 2}}})
  => "[2](#create)")


^{:refer hydrox.doc.link.stencil/link-stencil :added "0.1"}
(fact "links extra information for using the stencil format"
  (link-stencil
   {:articles {"example" {:meta {:name "world"}
                          :elements [{:type :paragraph
                                      :text "{{PROJECT.version}} {{DOCUMENT.name}}"}
                                     {:type :paragraph
                                      :text "{{hello}} {{example.hello.label}}"}]}}
    :project {:version "0.1"}
    :anchors {"example" {"hello" {:number 2
                                  :label "two"}}}}
   "example")
  => {:articles {"example" {:meta {:name "world"},
                            :elements ({:type :paragraph, :text "0.1 world"}
                                       {:type :paragraph, :text "2 two"})}},
      :project {:version "0.1"},
      :anchors {"example" {"hello" {:number 2, :label "two"}}}})
