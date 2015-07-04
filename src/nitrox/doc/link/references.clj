(ns nitrox.doc.link.references
  (:require [hara.data.nested :as nested]
            [rewrite-clj.node :as node]
            [clojure.string :as string]))

(defn process-doc-nodes [docs]
  (->> docs
       (filter (fn [x] (and (not (node/whitespace? x))
                            (not (string? (node/value x))))))
       (map node/string)
       (string/join "\n")))

(defn link-references [{:keys [references] :as folio} name]
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
