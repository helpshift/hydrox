(ns hydrox.doc.link.stencil
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

(defn transform-stencil
  "takes a short-form and expands using anchor information
   (transform-stencil \"{{hello}}\" \"example\"
                      {\"example\" {\"hello\" {:number 1}}})
   => \"1\"
 
   (transform-stencil \"{{stuff/create}}\" \"example\"
                      {\"stuff\" {\"create\" {:number 2}}})
   => \"2\"
 
   (transform-stencil \"[[stuff/create]]\" \"example\"
                      {\"stuff\" {\"create\" {:number 2}}})
   => \"[2](stuff.html#create)\"
 
   (transform-stencil \"[[create]]\" \"example\"
                      {\"example\" {\"create\" {:number 2}}})
   => \"[2](#create)\""
  {:added "0.1"}
  [string name tags]
  (-> string
      (.replaceAll full-citation-pattern "[{{$1/$2}}]($1.html#$2)")
      (.replaceAll short-citation-pattern "[{{$1}}](#$1)")
      (.replaceAll full-pattern  "{{$1.$2.number}}")
      (.replaceAll short-pattern (str "{{" name ".$1.number}}"))
      (stencil/render-string tags)))

(defn link-stencil
  "links extra information for using the stencil format
   (link-stencil
    {:articles {\"example\" {:meta {:name \"world\"}
                           :elements [{:type :paragraph
                                       :text \"{{PROJECT.version}} {{DOCUMENT.name}}\"}
                                      {:type :paragraph
                                       :text \"{{hello}} {{example.hello.label}}\"}]}}
     :project {:version \"0.1\"}
    :anchors {\"example\" {\"hello\" {:number 2
                                   :label \"two\"}}}}
    \"example\")
   => {:articles {\"example\" {:meta {:name \"world\"},
                             :elements ({:type :paragraph, :text \"0.1 world\"}
                                        {:type :paragraph, :text \"2 two\"})}},
       :project {:version \"0.1\"},
       :anchors {\"example\" {\"hello\" {:number 2, :label \"two\"}}}}"
  {:added "0.1"}
  [folio name]
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
