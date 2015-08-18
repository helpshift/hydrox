(ns hydrox.analyse.test.common
  (:require [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.string :as string]))

(defmulti frameworks (fn [sym] sym))
(defmethod frameworks :default [_])

(defmulti analyse-test (fn [type zloc] type))

(defn gather-meta
  "gets the metadata for a particular form
   (-> (z/of-string \"^{:refer clojure.core/+ :added \\\"0.1\\\"}\\n(fact ...)\")
       z/down z/right z/down
       gather-meta)
   => '{:added \"0.1\", :ns clojure.core, :var +, :refer clojure.core/+}"
  {:added "0.1"}
  [zloc]
  (if (-> zloc source/up source/up source/tag (= :meta))
    (let [mta (-> zloc source/up source/left source/sexpr)
          sym (:refer mta)]
      (if sym
        (assoc mta
               :ns   (symbol (str (.getNamespace sym)))
               :var  (symbol (name sym)))))))


(defn gather-string
  "creates correctly spaced code string from normal docstring
   
   (-> (z/of-string \"\\\"hello\\nworld\\nalready\\\"\")
       (gather-string)
       (str))
   => \"hello
   world
   already\""
  {:added "0.1"}
  [zloc]
  (node/string-node (->> (source/sexpr zloc)
                         (string/split-lines)
                         (map-indexed (fn [i s]
                                        (str (if (zero? i) "" "  ")
                                             (string/triml s))) ))))

(defn strip-quotes
  "takes away the quotes from a string for formatting purposes
 
   (strip-quotes \"\\\"hello\\\"\")
   => hello"
  {:added "0.1"}
  [s]
  (if (and (.startsWith s "\"")
           (.endsWith s "\""))
    (subs s 1 (dec (count s)))
    s))

(defn join-nodes [nodes]
  (->> nodes
       (map node/string)))
