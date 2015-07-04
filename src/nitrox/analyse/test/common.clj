(ns nitrox.analyse.test.common
  (:require [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.string :as string]))

(defmulti frameworks (fn [sym] sym))
(defmethod frameworks :default [_])

(defmulti analyse-test (fn [type zloc] type))

(defn gather-meta [zloc]
  (if (-> zloc source/up source/up source/tag (= :meta))
    (let [mta (-> zloc source/up source/left source/sexpr)
          sym (:refer mta)]
      (if sym
        (assoc mta
               :ns   (symbol (str (.getNamespace sym)))
               :var  (symbol (name sym)))))))


(defn gather-string [zloc]
  (node/string-node (->> (source/sexpr zloc)
                         (string/split-lines)
                         (map-indexed (fn [i s]
                                        (str (if (zero? i) "" "  ")
                                             (string/triml s))) ))))

(defn strip-quotes
  [s]
  (if (and (.startsWith s "\"")
           (.endsWith s "\""))
    (subs s 1 (dec (count s)))
    s))

(defn join-nodes [nodes]
  (->> nodes
       (map node/string)))
