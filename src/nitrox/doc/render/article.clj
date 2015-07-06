(ns nitrox.doc.render.article
  (:require [nitrox.doc.render.common :as common]))

(defmethod common/render :article
  [element folio]
  (vec (concat [:div {:class :article}]
               (map (fn [ele] (common/render ele folio))
                    (:elements element)))))
