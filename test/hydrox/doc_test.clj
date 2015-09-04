(ns hydrox.doc-test
  (:use midje.sweet)
  (:require [hydrox.doc :refer :all]
            [hydrox.analyse :as analyser]))

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

(comment

  (use 'hydrox.core)

  (def reg (single-use "../../chit/hara/project.clj"))

  
  
  (keys @(:state reg))
  (:meta :articles :namespaces :project :references :registry :root :namespace-lu)
  (-> @(:state reg) :references (get-in ['hara.group 'defgroup]))
  (import-docstring reg)
  (import-docstring reg 'hara.group)
  (generate-docs )

  
  (-> (prepare-article
       (-> hydrox.core.regulator/*running*
           first
           :state
           deref)
       "sample-document"
       "test/documentation/sample_document.clj")
      :anchors-lu
      (get-in [:articles "sample-document" :elements])))
