(ns nitrox.analyser.doc.link.stencil
  (:require [clojure.string :as string]
            [stencil.core :as stencil]))

(def full-pattern
  (string/join ["\\{\\{"
                "([^/^\\.^\\{^\\}]+)"
                "/"
                "([^/^\\.^\\{^\\}]+)"
                "\\}\\}"]))

(def short-pattern
  (string/join ["\\{\\{"
                "([^/^\\.^\\{^\\}]+)"
                "\\}\\}"]))

(defn transform-stencil [string name tags]
  (-> string
      (.replaceAll full-pattern  "{{$1.$2.number}}")
      (.replaceAll short-pattern (str "{{" name ".$1.number}}"))
      (stencil/render-string tags)))

(defn link-stencil [folio name]
  (let [anchors (:anchors folio)]
    (update-in folio [:articles name :elements]
               (fn [elements]
                 (->> elements
                      (map (fn [element]
                             (if (= :paragraph (:type element))
                               (update-in element [:text]
                                          transform-stencil name anchors)
                               element))))))))
