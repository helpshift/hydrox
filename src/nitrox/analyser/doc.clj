(ns nitrox.analyser.doc
  (:require [rewrite-clj.zip :as source]
            [jai.query :as query]
            [nitrox.analyser.common :as common]
            [nitrox.analyser.doc.parse :as parse]
            [nitrox.analyser.doc.collect :as collect]
            [nitrox.analyser.doc.link :as link]))

(defn generate-article [name file folio]
  (let [elements (parse/parse-file file folio)]
    (-> (assoc-in folio [:articles name :elements] elements)
        (collect/collect-global name)
        (collect/collect-article name)
        (collect/collect-namespaces name)
        (collect/collect-tags name)
        (link/link-namespaces name)
        (link/link-numbers name)
        (link/link-references name)
        (link/link-tags name))))


(comment
  (require '[rewrite-clj.node :as node])
  
  (get-in
   (generate-article "sample" "test/documentation/sub_test.clj" @(:state nitrox.regulator/reg))
   [:articles "sample"])

  (get-in
   (generate-article "sample" "test/documentation/example_test.clj" @(:state nitrox.regulator/reg))
   [:articles "sample"])
  
  


  (filter (fn [x] (and (not (node/whitespace? x))
                      (not (string? (node/value x)))))
   (-> @(:state nitrox.regulator/reg) :references (get-in '[nitrox.analyser.test find-frameworks :docs])))
  
  (-> @(:state nitrox.regulator/reg) :references)
  

  (+ 1 1))
