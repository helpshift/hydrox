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
   => \"(+\\n  1\\n  2)\""
  {:added "0.1"}
  [zloc node]
  (if node
    (-> zloc
        (zip/insert-right node)
        (zip/insert-right (node/whitespace-node "  "))
        (zip/insert-right (node/newline-node "\n")))
    zloc))

(defn has-quotes?
  "checks if a string has quotes
 
   (has-quotes? \"\\\"hello\\\"\")
   => true"
  {:added "0.1"}
  [s]
  (and (.startsWith s "\"")
           (.endsWith s "\"")))

(defn strip-quotes
  "gets rid of quotes in a string
 
   (strip-quotes \"\\\"hello\\\"\")
   => \"hello\""
  {:added "0.1"}
  [s]
  (if (has-quotes? s) 
    (subs s 1 (dec (count s)))
    s))

(defn escape-newlines
  "makes sure that newlines are printable
 
   (escape-newlines \"\\\n\")
   => \"\\n\""
  {:added "0.1"}
  [s]
  (-> s
      (.replaceAll "\\n" "\\\\n")))

(defn escape-escapes
  "makes sure that newlines are printable
 
   (escape-escapes \"\\\n\")
   => \"\\\n\""
  {:added "0.1"}
  [s]
  (-> s
      (.replaceAll "(\\\\)([A-Za-z])" "$1$1$2")))

(defn escape-quotes
  "makes sure that quotes are printable in string form
 
   (escape-quotes \"\\\"hello\\\"\")
   => \"\\\"hello\\\"\""
  {:added "0.1"}
  [s]
  (-> s
      (.replaceAll "(\\\\)?\"" "$1$1\\\\\\\"")))

(defn strip-quotes-array
  "utility that strips quotes when not the result of a fact
   (strip-quotes-array [\"\\\"hello\\\"\"])
   => [\"hello\"]
   
   (strip-quotes-array [\"(str \\\"hello\\\")\" \" \" \"=>\" \" \" \"\\\"hello\\\"\"])
   => [\"(str \\\"hello\\\")\" \" \" \"=>\" \" \" \"\\\"hello\\\"\"]"
  {:added "0.1"}
  ([arr] (strip-quotes-array arr nil nil []))
  ([[x & more] p1 p2 out]
   (cond (nil? x)
         out

         :else
         (recur more x p1 (conj out (if (= p2 "=>")
                                      (if (has-quotes? x)
                                        (escape-newlines x)
                                        x)
                                      (strip-quotes x)))))))

(defn nodes->docstring
  "converts nodes to a docstring compatible
   (->> (z/of-string \"\\\"hello\\\"\\n  (+ 1 2)\\n => 3 \")
        (iterate z/right*)
        (take-while identity)
        (map z/node)
        (nodes->docstring)
        (node/string))
   => \"\"hello\\n  (+ 1 2)\\n  => 3 \"\"
 
   (->> (z/of-string (str [\\e \\d]))
        (iterate z/right*)
        (take-while identity)
        (map z/node)
        (nodes->docstring)
        (str)
        (read-string))
  => \"[\\e \\d]\""
  {:added "0.1"}
  [nodes]
  (->> nodes
       (map node/string)
       (strip-quotes-array)
       (string/join)
       (escape-escapes)
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
 
   (->> (all-files {:root (.getCanonicalPath (io/file \"example\"))} :root \"md\")
        (map #(.getName %)))
   => [\"README.md\"]"
  {:added "0.1"}
  [project path-type extension]
  (->> project
       path-type
       (#(if (sequential? %) % [%]))
       (mapcat (fn [dir] (file-seq (io/file (if (.startsWith dir "/")
                                              dir
                                              (str (:root project) "/" dir))))))
       (filter (fn [file] (and (.isFile file)
                               (.endsWith (str file) extension))))
       (map #(.getCanonicalFile %))))
