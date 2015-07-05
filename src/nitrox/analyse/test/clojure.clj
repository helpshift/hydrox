(ns nitrox.analyse.test.clojure
  (:require [jai.query :as query]
            [nitrox.analyse.test.common :as common]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.string :as string]))

(defn gather-is-form
  ([zloc]
   (let [zloc (-> zloc source/down source/right)]
     (cond (query/match zloc '(= _ _))
           (let [zloc (-> zloc source/down source/right)]
             [(source/node zloc)
              (node/newline-node "\n")
              (node/whitespace-node "  ")
              (node/token-node '=> "=>")
              (node/whitespace-node " ")
              (source/node (source/right zloc))])

           :else
           [(source/node zloc)
            (node/newline-node "\n")
            (node/whitespace-node "  ")
            (node/token-node '=> "=>")
            (node/whitespace-node " ")
            (node/token-node true "true")]))))

(defn gather-deftest-body
  ([zloc]
     (gather-deftest-body zloc []))
  ([zloc output]
     (cond (nil? zloc) output
           
           (and (= :meta (source/tag zloc))
                (= [:token :hidden] (source/value zloc)))
           output
           
           (query/match zloc string?)
           (recur (source/right* zloc)
                  (conj output (common/gather-string zloc)))

           (query/match zloc 'is)
           (recur (source/right* zloc) (vec (concat output (gather-is-form zloc))))
           
           :else
           (recur (source/right* zloc) (conj output (source/node zloc))))))

(defn gather-deftest
  [zloc]
  (if-let [mta (common/gather-meta zloc)]
    (assoc mta :docs (gather-deftest-body zloc))))

(defmethod common/frameworks 'clojure.test [_] :clojure)

(defmethod common/analyse-test :clojure
  [type zloc]
  (let [fns  (query/$ zloc [(deftest _ | & _)] {:return :zipper :walk :top})]
    (->> (keep gather-deftest fns)
         (reduce (fn [m {:keys [ns var docs] :as meta}]
                   (-> m
                       (assoc-in [ns var :docs] docs)
                       (assoc-in [ns var :meta] (dissoc meta :docs :ns :var :refer))))
                 {}))))
