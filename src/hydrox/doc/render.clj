(ns hydrox.doc.render
  (:require [hydrox.doc.render
             [article :as article]
             [navigation :as navigation]
             [toc :as toc]]
            [hiccup.compiler :as compiler]
            [clojure.string :as string]))

(defn render-article [{:keys [elements]} folio]
  (->> elements
       (map #(article/render % folio))
       (#'compiler/compile-seq)
       (string/join)))

(defn render-navigation [{:keys [elements]} folio]
  (let [chapters (filter (fn [e] (#{:chapter :appendix} (:type e)))
                         elements)]
    (->> chapters
         (map #(navigation/render % folio))
         (#'compiler/compile-seq)
         (string/join))))
