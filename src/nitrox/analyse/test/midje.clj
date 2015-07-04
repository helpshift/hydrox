(ns nitrox.analyse.test.midje
  (:require [jai.query :as query]
            [clojure.string :as string]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [nitrox.analyse.test.common :as common]))

(defn gather-fact-body
  "collects the required elements from the fact body"
  ([zloc]
     (gather-fact-body zloc []))
  ([zloc output]
     (cond (nil? zloc) output
           
           (and (= :meta (source/tag zloc))
                (= [:token :hidden] (source/value zloc)))
           output

           (query/match zloc string?)
           (recur (source/right* zloc)
                  (conj output (common/gather-string zloc)))
           
           :else
           (recur (source/right* zloc) (conj output (source/node zloc))))))

(defn gather-fact
  "collects information from a fact form"
  [zloc]
  (if-let [mta (common/gather-meta zloc)]
    (assoc mta :docs (gather-fact-body zloc))))

(defmethod common/frameworks 'midje.sweet  [_]  :midje)

(defmethod common/analyse-test :midje
  ([type zloc]
   (let [fns  (query/$ zloc [(fact | & _)] {:return :zipper :walk :top})]
     (->> (keep gather-fact fns)
          (reduce (fn [m {:keys [ns var docs] :as meta}]
                    (-> m
                        (assoc-in [ns var :docs] docs)
                        (assoc-in [ns var :meta] (dissoc meta :docs :ns :var :refer))))
                  {})))))
