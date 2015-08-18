(ns hydrox.gulfstream-test
  (:use midje.sweet)
  (:require [hydrox.doc :as doc]
            [hydrox.core :as core]
            [clojure.java.io :as io]
            ))

(comment

  (defn read-proj [file]
    (let [proj  (core/read-project (io/file file))
                 folio (-> proj
                           (core/create-folio)
                           (core/init-folio))
                 state (atom folio)]
      (core/regulator state proj)))
      (def reg (read-proj "project.clj"))

  (do (def reg (read-proj "../../helpshift/gulfstream/project.clj"))
      (core/import-docstring reg))
  )
