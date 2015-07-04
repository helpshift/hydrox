(ns nitrox.doc.link.namespaces)

(defn link-namespaces [{:keys [namespaces articles] :as folio} name]
  (update-in folio [:articles name :elements]
             (fn [elements]
               (mapv (fn [element]
                       (if (= :ns (:type element))
                         (let [{:keys [ns code]} element]
                           (assoc element
                                  :type :code
                                  :origin :ns
                                  :indentation 0
                                  :code (get-in namespaces [ns :code])))
                         element))
                     elements))))
