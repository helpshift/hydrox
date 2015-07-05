(ns nitrox.meta.util-test
  (:use midje.sweet)
  (:require [nitrox.meta.util :refer :all]
            [clojure.java.io :as io]
            [rewrite-clj.zip :as z]
            [rewrite-clj.node :as node]))

^{:refer nitrox.meta.util/append-node :added "0.1"}
(fact "Adds node as well as whitespace and newline on right"

  (-> (z/of-string "(+)")
      (z/down)
      (append-node 2)
      (append-node 1)
      (z/->root-string))
  => "(+\n  1\n  2)")

^{:refer nitrox.meta.util/strip-quotes :added "0.1"}
(fact "gets rid of quotes in a string"

  (strip-quotes "\"hello\"")
  => "hello")

^{:refer nitrox.meta.util/escape-quotes :added "0.1"}
(fact "makes sure that quotes are printable in string form"

  (escape-quotes "\"hello\"")
  => "\\\"hello\\\"")

^{:refer nitrox.meta.util/nodes->docstring :added "0.1"}
(fact "converts nodes to a docstring compatible"
  (->> (z/of-string "\"hello\"\n  (+ 1 2)\n => 3 ")
       (iterate z/right*)
       (take-while identity)
       (map z/node)
       (nodes->docstring)
       (node/string))
  => "\"hello\n   (+ 1 2)\n  => 3 \"")

^{:refer nitrox.meta.util/import-location :added "0.1"}
(fact "imports the meta information and docstring")

^{:refer nitrox.meta.util/write-to-file :added "0.1"}
(fact "exports the zipper contents to file")

^{:refer nitrox.meta.util/all-files :added "0.1"}
(fact "finds all files in the project given a context"

  (->> (all-files {:root (.getCanonicalPath (io/file "example"))} :root ".md")
       (map #(.getName %)))
  => ["README.md"])

