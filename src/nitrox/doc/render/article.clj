(ns nitrox.doc.render.article
  (:require [nitrox.doc.render.common :as common]))

(defmethod common/render :article
  [element folio]
  (vec (concat [:html]
               (map common/render (:elements element)))))
