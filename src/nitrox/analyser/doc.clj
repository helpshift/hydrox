(ns nitrox.analyser.doc
  (:require [rewrite-clj.zip :as source]
            [jai.query :as query]
            [nitrox.analyser.common :as common]
            [nitrox.analyser.doc.parse :as parse]
            [nitrox.analyser.doc.collect :as collect]))

(defmethod common/analyse-file :doc [_ file opts]
  [file]
  (let [elements (parse/parse-file file opts)]
    (-> (parse/parse-file file opts)
        (collect ))))
