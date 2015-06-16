(ns nitrox.code.util
  (:require [clojure.zip :as zip]
            [clojure.string :as string]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.java.io :as io]))

(defn append-node
  [zloc node]
  (if node
    (-> zloc
        (zip/insert-right node)
        (zip/insert-right (node/whitespace-node "  "))
        (zip/insert-right (node/newline-node "\n")))
    zloc))

(defn strip-quotes
  [s]
  (if (and (.startsWith s "\"")
           (.endsWith s "\""))
    (subs s 1 (dec (count s)))
    s))

(defn escape-quotes
  [s]
  (-> s
      (.replaceAll "(\\\\)?\"" "$1$1\\\\\\\"")))

(defn nodes->docstring
  [nodes]
  (->> nodes
       (map node/string)
       (map strip-quotes)
       (string/join)
       (escape-quotes)
       (string/split-lines)
       (map-indexed (fn [i s]
                      (str (if-not (or (zero? i)
                                       (= i (dec (count nodes))))
                             " ")
                           s)))
       (node/string-node)))

(defn import-location [zloc nsp gathered]
  (let [sym   (source/sexpr zloc)
        nodes (get-in gathered [nsp sym :docs])
        meta  (get-in gathered [nsp sym :meta])]
    (-> zloc
        (append-node meta)
        (append-node (if nodes (nodes->docstring nodes))))))

(defn write-to-file [zloc file]
  (->> (iterate source/right* zloc)
       (take-while identity)
       (map source/node)
       (map node/string)
       (string/join)
       (spit file)))

(defn all-files
  "finds all files in the project given a context
 
   (->> (all-files (project/read \"project.clj\") :root \".md\")
        (map #(.getName %)))
   => [\"README.md\" \"TEST.md\"]
 
   (->> (all-files (project/read \"project.clj\") :source-paths \".md\")
        (map #(.getName %)))
   => ()
 
   (->> (all-files (project/read \"project.clj\") :source-paths \".clj\")
        (count))
   => (comp not zero?)"
  {:added "0.2"}
  [project path-type extension]
  (->> project
       path-type
       (#(if (seq? %) % [%]))
       (mapcat (fn [dir] (file-seq (io/file dir))))
       (filter (fn [file] (and (.isFile file)
                               (.endsWith (str file) extension))))
       (map #(.getCanonicalFile %))))

