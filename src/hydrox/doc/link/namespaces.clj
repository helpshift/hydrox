(ns hydrox.doc.link.namespaces)

(defn link-namespaces
  "link elements with `:ns` forms to code
 
   (link-namespaces
    {:articles {\"example\" {:elements [{:type :ns :ns \"clojure.core\"}]}}
     :namespaces {\"clojure.core\" {:code \"(ns clojure.core)\"}}}
    \"example\")
   => {:articles {\"example\" {:elements [{:type :code
                                         :ns \"clojure.core\"
                                        :origin :ns
                                         :indentation 0
                                         :code \"(ns clojure.core)\"}]}}
       :namespaces {\"clojure.core\" {:code \"(ns clojure.core)\"}}}"
  {:added "0.1"}
  [{:keys [namespaces articles] :as folio} name]
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
