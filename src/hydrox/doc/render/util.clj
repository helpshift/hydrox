(ns hydrox.doc.render.util
  (:require [clojure.string :as string]
            [markdown.core :as markdown]
            [hiccup.core :as html]))

(defn adjust-indent
  "fixes indentation for code that is off slightly due to alignment
 
   (adjust-indent \"(+ 1\\n     2)\" 2)
   => \"(+ 1\\n  2)\""
  {:added "0.1"}
  [s spaces]
  (->> (string/split-lines s)
       (map (fn [line]
              (if (and (< spaces (count line))
                       (re-find #"^\s+$" (subs line 0 spaces)))
                (subs line spaces)
                line)))
       (string/join "\n")))

(defn basic-html-escape
  "escapes characters using standard html format
 
   (basic-html-escape \"<>\")
   => \"&lt;&gt;\""
  {:added "0.1"}
  [data]
  (clojure.string/escape data { \< "&lt;" \> "&gt;" \& "&amp;" \" "&quot;" \\ "&#92;"}))

(defn basic-html-unescape
  "unescapes characters with standard html format
 
   (basic-html-unescape \"&amp;quot;\")
   => \"&quot;\""
  {:added "0.1"}
  [data]
  (let [out (-> data
                (.replaceAll "&amp;quot;" "&quot;")
                (.replaceAll "&amp;lt;" "&lt;")
                (.replaceAll "&amp;gt;" "&gt;")
                (.replaceAll "&amp;amp;" "&amp;"))]
    out))

(defn markup
  "calls the markdown library to create markup from a string"
  {:added "0.1"}
  [data]
  (markdown/md-to-html-string data))

(defn join
  "like string/join but will return the input if it is a string
 
   (join \"hello\") => \"hello\"
 
   (join [\"hello\" \" \" \"world\"]) => \"hello world\""
  {:added "0.1"}
  [data]
  (cond (string? data)
        data

        (vector? data)
        (string/join data)))
