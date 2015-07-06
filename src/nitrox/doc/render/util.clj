(ns nitrox.doc.render.util
  (:require [clojure.string :as string]
            [markdown.core :as markdown]
            [hiccup.core :as html]))

(defn adjust-indent [s spaces]
  (->> (string/split-lines s)
       (map (fn [line]
              (if (and (< spaces (count line))
                       (re-find #"^\s+$" (subs line 0 spaces)))
                (subs line spaces)
                line)))
       (string/join "\n")))

(defn basic-html-escape
  [data]
  (clojure.string/escape data { \< "&lt;" \> "&gt;" \& "&amp;" \" "&quot;" }))

(defn basic-html-unescape
  [data]
  (let [out (-> data
                (.replaceAll "&amp;quot;" "&quot;")
                (.replaceAll "&amp;lt;" "&lt;")
                (.replaceAll "&amp;gt;" "&gt;")
                (.replaceAll "&amp;amp;" "&amp;"))]
    out))

(defn markup [data]
  (markdown/md-to-html-string data))
