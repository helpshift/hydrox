(ns nitrox.doc.link.stencil
  (:require [clojure.string :as string]
            [stencil.core :as stencil]))

(def full-citation-pattern
  (string/join ["\\[\\["
                "([^/^\\.^\\{^\\}^\\[^\\]]+)"
                "/"
                "([^/^\\.^\\{^\\}^\\[^\\]]+)"
                "\\]\\]"]))

(def short-citation-pattern
  (string/join ["\\[\\["
                "([^/^\\.^\\{^\\}^\\[^\\]]+)"
                "\\]\\]"]))

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
      (.replaceAll full-citation-pattern "[[{{$1/$2}}](#$1)]")
      (.replaceAll short-citation-pattern "[[{{$1}}](#$1)]")
      (.replaceAll full-pattern  "{{$1.$2.number}}")
      (.replaceAll short-pattern (str "{{" name ".$1.number}}"))
      (stencil/render-string tags)))

(defn link-stencil [folio name]
  (let [anchors (assoc (:anchors folio)
                       :PROJECT (:project folio)
                       :DOCUMENT (get-in folio [:articles name :meta]))]
    (update-in folio [:articles name :elements]
               (fn [elements]
                 (->> elements
                      (map (fn [element]
                             (if (= :paragraph (:type element))
                               (update-in element [:text]
                                          transform-stencil name anchors)
                               element))))))))
