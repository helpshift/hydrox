(ns hydrox.doc-test
  (:use midje.sweet)
  (:require [hydrox.doc :refer :all]))

^{:refer hydrox.doc/prepare-article :added "0.1"}
(fact "generates the flat outline for rendering")

^{:refer hydrox.doc/generate :added "0.1"}
(fact "generates the tree outline for rendering")

^{:refer hydrox.doc/prepare-includes :added "0.1"}
(fact "prepare template accept includes")

^{:refer hydrox.doc/find-includes :added "0.1"}
(fact "finds elements with `@=` tags"

  (find-includes "<@=hello> <@=world>")
  => #{:hello :world})

^{:refer hydrox.doc/render-entry :added "0.1"}
(fact "helper function that is called by both render-single and render-all")

^{:refer hydrox.doc/copy-files :added "0.1"}
(fact "copies all files from the template directory into the output directory")

^{:refer hydrox.doc/render-single :added "0.1"}
(fact "render for a single entry in the project.clj map")

^{:refer hydrox.doc/render-all :added "0.1"}
(fact "render for all documentation entries in the project.clj map")
