(ns hydrox.doc
  (:require [rewrite-clj.zip :as source]
            [jai.query :as query]
            [hydrox.common.util :as util]
            [hydrox.doc
             [collect :as collect]
             [link :as link]
             [parse :as parse]
             [render :as render]
             [structure :as structure]]))

(defn prepare-article
  "generates the flat outline for rendering"
  {:added "0.1"}
  [folio name file]
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

(defn generate
  "generates the tree outline for rendering"
  {:added "0.1"}
  [{:keys [project] :as folio} name]
  (let [meta       (-> project :documentation :files (get name))
        folio      (prepare-article folio name (:input meta))
        elements   (get-in folio [:articles name :elements])
        structure  (structure/structure elements)]
    structure))

(defn find-includes
  "finds elements with `@=` tags
 
   (find-includes \"<@=hello> <@=world>\")
   => #{:hello :world}"
  {:added "0.1"}
  [html]
  (->> html
       (re-seq #"<@=([^>^<]+)>")
       (map second)
       (map keyword)
       set))

(defn prepare-includes
  "prepare template accept includes"
  {:added "0.1"}
  [name includes folio]
  (let [no-doc (->> (filter (fn [[k v]] (#{:article :navigation} v)) includes)
                    empty?)]
    (cond no-doc
          includes

          :else
          (let [elements (generate folio name)]
            (reduce-kv (fn [out k v]
                         (assoc out k (case v
                                        :article    (render/render-article elements folio)
                                        :navigation (render/render-navigation elements folio)
                                        v)))
                       {}
                       includes)))))

(defn render-entry
  "helper function that is called by both render-single and render-all"
  {:added "0.1"}
  [name entry folio]
  (println "Rendering" name)
  (let [project        (:project folio)
        opts           (:documentation project)
        entry          (merge (util/filter-strings project)
                              (-> opts :template :defaults)
                              entry)
        template-path  (util/full-path (:template entry) (-> opts :template :path) project)
        output-path    (util/full-path (str name ".html") (:output opts) project)
        template       (slurp template-path)
        includes       (->> (find-includes template)
                            (select-keys entry))
        includes       (prepare-includes name includes folio)
        html           (render/replace-template template includes opts project)]
    (spit output-path html)))

(defn render-single
  "render for a single entry in the project.clj map"
  {:added "0.1"}
  [folio name]
  (let [opts (-> folio :project :documentation)
        entry (get-in opts [:files name])]
    (render-entry name entry folio)))

(defn render-all
  "render for all documentation entries in the project.clj map"
  {:added "0.1"}
  [folio]
  (let [opts (-> folio :project :documentation)]
    (doseq [[name entry] (:files opts)]
      (render-entry name entry folio))))
