(ns nitrox.doc-test
  (:use midje.sweet)
  (:require [nitrox.doc :refer :all]
            [nitrox.core :as core]
            [clojure.java.io :as io]
            [hiccup.core :as html]))


(def reg (let [proj  (core/read-project (io/file "example/project.clj"))
               folio (-> proj
                         (core/create-folio)
                         (core/init-folio))
               state (atom folio)]
           (core/regulator state proj)))

(comment ""
         (spit "logic.html" (-> (html/html (generate @(:state reg) "logic"))))
         
  )
