(ns nitrox.doc.render
  (:require [hara.namespace.import :as ns]
            [nitrox.doc.render
             [common :as common]
             article toc]))

(defn render [element folio]
  (common/render element folio))
