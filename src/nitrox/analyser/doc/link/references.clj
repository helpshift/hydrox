(ns nitrox.analyser.doc.link.references
  (:require [hara.data.nested :as nested]))

(defn find-reference [{:keys [refer mode] :as element} references]
  (let [nsp (symbol (.getNamespace refer))
        var (symbol (name refer))]
    (assoc element
           :type :code
           :origin :reference
           :indentation 0
           :code [(or (get-in references [nsp var (or mode :source)]) "")])))

(defn link-references [elements references]
  (mapv (fn [ele]
          (if (= :reference (:type ele))
            (find-reference ele references)
            ele))
        elements))
