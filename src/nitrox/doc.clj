(ns nitrox.doc
  (:require [rewrite-clj.zip :as source]
            [jai.query :as query]
            [nitrox.doc
             [collect :as collect]
             [link :as link]
             [parse :as parse]
             [render :as render]
             [structure :as structure]]))

(defn prepare-article [folio name file]
  (let [elements (parse/parse-file file folio)]
    (-> (assoc-in folio [:articles name :elements] elements)
        (collect/collect-global name)
        (collect/collect-article name)
        (collect/collect-namespaces name)
        (collect/collect-tags name)
        (link/link-namespaces name)
        (link/link-numbers name)
        (link/link-references name)
        (link/link-tags name)
        (link/link-anchors-lu name)
        (link/link-anchors name)
        (link/link-stencil name)
        (collect/collect-citations name))))

(defn generate [{:keys [project] :as folio} name]
  (let [meta  (-> project :documentation :files (get name))
        folio (prepare-article folio name (:input meta))
        elements (get-in folio [:articles name :elements])]
    (-> elements
        (structure/structure)
        (render/render folio))))

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
   (-> @(:state nitrox.regulator/reg) :references (get-in '[nitrox.analyse.test find-frameworks :docs])))

  (-> @(:state nitrox.regulator/reg) :references)


  (+ 1 1))
