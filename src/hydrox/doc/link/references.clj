(ns hydrox.doc.link.references
  (:require [hara.data.nested :as nested]
            [rewrite-clj.node :as node]
            [clojure.string :as string]))

(defn process-doc-nodes
  "treat test nodes specially when rendering code
 
   (->> (z/of-string \"(+ 1 1) => (+ 2 2)\")
        (iterate z/right*)
        (take-while identity)
        (map z/node)
        (process-doc-nodes))
   => \"(+ 1 1)\\n  =>\\n  (+ 2 2)\""
  {:added "0.1"}
  [docs]
  (->> docs
       (filter (fn [x] (and (not (node/whitespace? x))
                            (not (string? (node/value x))))))
       (map node/string)
       (string/join "\n")))

(defn link-references
  "link code for elements to references
 
   (link-references {:articles {\"example\" {:elements [{:type :reference :refer 'example.core/hello}]}}
                     :references '{example.core {hello {:docs []
                                                        :source \"(defn hello [] 1)\"}}}}
                    \"example\")
   => '{:articles {\"example\"
                   {:elements [{:type :code,
                               :refer example.core/hello,
                                :origin :reference,
                                :indentation 0,
                                :code \"(defn hello [] 1)\",
                                :mode :source}]}},
        :references {example.core {hello {:docs [], :source \"(defn hello [] 1)\"}}}}"
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
                               code (case mode
                                      :source (get-in references [nsp var mode])
                                      :docs   (-> (get-in references [nsp var mode])
                                                  (process-doc-nodes)))]
                           
                           (assoc element
                                  :type :code
                                  :origin :reference
                                  :indentation 0
                                  :code code
                                  :mode mode))
                         element))
                     elements))))
