(ns hydrox.meta.util
  (:require [clojure.zip :as zip]
            [clojure.string :as string]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.java.io :as io]))

(defn append-node
  "Adds node as well as whitespace and newline on right
 
   (-> (z/of-string \"(+)\")
       (z/down)
       (append-node 2)
       (append-node 1)
       (z/->root-string))
   => (+
  1
   2)"
  {:added "0.1"}
  [zloc node]
  (if node
    (-> zloc
        (zip/insert-right node)
        (zip/insert-right (node/whitespace-node "  "))
        (zip/insert-right (node/newline-node "\n")))
    zloc))

(defn strip-quotes
  "gets rid of quotes in a string
 
   (strip-quotes \"\\\"hello\\\"\")
   => hello"
  {:added "0.1"}
  [s]
  (if (and (.startsWith s "\"")
           (.endsWith s "\""))
    (subs s 1 (dec (count s)))
    s))

(defn escape-quotes
  "makes sure that quotes are printable in string form
 
   (escape-quotes \"\\\"hello\\\"\")
   => \\\"hello\\\""
  {:added "0.1"}
  [s]
  (-> s
      (.replaceAll "(\\\\)?\"" "$1$1\\\\\\\"")))

(defn nodes->docstring
  "converts nodes to a docstring compatible
   (->> (z/of-string \"\\\"hello\\\"\n  (+ 1 2)\n => 3 \")
        (iterate z/right*)
        (take-while identity)
        (map z/node)
        (nodes->docstring)
        (node/string))
   => \"hello
  (+ 1 2)
   => 3 \""
  {:added "0.1"}
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

(defn import-location
  "imports the meta information and docstring"
  {:added "0.1"}
  [zloc nsp gathered]
  (let [sym   (source/sexpr zloc)
        nodes (get-in gathered [nsp sym :docs])
        meta  (get-in gathered [nsp sym :meta])]
    (-> zloc
        (append-node meta)
        (append-node (if nodes (nodes->docstring nodes))))))

(defn write-to-file
  "exports the zipper contents to file"
  {:added "0.1"}
  [zloc file]
  (->> (iterate source/right* zloc)
       (take-while identity)
       (map source/node)
       (map node/string)
       (string/join)
       (spit file)))

(defn all-files
  "finds all files in the project given a context
 
   (->> (all-files {:root (.getCanonicalPath (io/file \"example\"))} :root \".md\")
        (map #(.getName %)))
   => [\"README.md\"]"
  {:added "0.2"}
  [project path-type extension]
  (->> project
       path-type
       (#(if (sequential? %) % [%]))
       (mapcat (fn [dir] (file-seq (io/file (str (:root project) "/" dir)))))
       (filter (fn [file] (and (.isFile file)
                               (.endsWith (str file) extension))))
       (map #(.getCanonicalFile %))))
