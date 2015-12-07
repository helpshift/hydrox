(ns hydrox.doc.link.references
  (:require [hara.data.nested :as nested]
            [hydrox.meta.util :as util]
            [rewrite-clj.node :as node]
            [clojure.string :as string]))

(defn process-doc-nodes
  "treat test nodes specially when rendering code
 
   (->> (z/of-string \"(+ 1 1) => (+ 2 2)\")
        (iterate z/right*)
        (take-while identity)
        (map z/node)
        (process-doc-nodes))
   => \"(+ 1 1) => (+ 2 2)\"
   "
  {:added "0.1"}
  [docs]
  (->> docs
       (map (fn [node]
              (let [res (node/string node)]
                (cond (and (not (node/whitespace? node))
                           (not (node/comment? node))
                           (string? (node/value node)))
                      (util/escape-newlines res)

                      :else res))))
       (string/join)))

(defn link-references
  "link code for elements to references
 
   (link-references {:articles {\"example\" {:elements [{:type :reference :refer 'example.core/hello}]}}
                     :references '{example.core {hello {:docs []
                                                        :source \"(defn hello [] 1)\"}}}}
                    \"example\")
   => {:articles
       {\"example\"
       {:elements
         '[{:type :code,
            :refer example.core/hello,
            :origin :reference,
            :indentation 0,
            :code \"(defn hello [] 1)\",
            :mode :source,
            :title \"source of <i>example.core/hello</i>\"}]}},
       :references '{example.core {hello {:docs [], :source \"(defn hello [] 1)\"}}}}"
  {:added "0.1"}
  [{:keys [references] :as folio} name]
  (update-in folio [:articles name :elements]
             (fn [elements]
               (mapv (fn [element]
                       (if (-> element :type (= :reference))
                         (let [{:keys [refer mode]} element
                               nsp (symbol (.getNamespace refer))
                               var (symbol (.getName refer))
                               mode (or mode :source)
                               code (get-in references [nsp var mode])
                               code (case mode
                                      :source code
                                      :docs   (process-doc-nodes code))]
                           (-> element
                               (assoc :type :code
                                      :origin :reference
                                      :indentation (case mode :source 0 :docs 2)
                                      :code code
                                      :mode mode)
                               (update-in [:title] #(or % (str (clojure.core/name mode) " of <i>" refer "</i>")))))
                         element))
                     elements))))
