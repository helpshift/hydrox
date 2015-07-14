(ns hydrox.doc.render
  (:require [hydrox.doc.render
             [article :as article]
             [navbar :as navbar]
             [toc :as toc]]
            [hiccup.compiler :as compiler]
            [clojure.string :as string]))

(defn render-article [{:keys [elements]} folio]
  (->> elements
       (map #(article/render % folio))
       (#'compiler/compile-seq)
       (string/join)))

(defn render-navbar [{:keys [elements]} folio]
  (let [chapters (filter (fn [e] (#{:chapter :appendix} (:type e)))
                         elements)]
    (->> chapters
         (map #(navbar/render % folio))
         (#'compiler/compile-seq)
         (string/join))))
