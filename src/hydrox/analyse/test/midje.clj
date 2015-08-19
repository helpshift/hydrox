(ns hydrox.analyse.test.midje
  (:require [jai.query :as query]
            [clojure.string :as string]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [hydrox.analyse.test.common :as common]))

(defn gather-fact-body
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
  "Make docstring notation out of fact form
   (-> \"^{:refer example/hello-world :added \\\"0.1\\\"}
        (fact \\\"Sample test program\\\"\\n  (+ 1 1) => 2\\n  (long? 3) => true)\"
       (z/of-string)
       z/down z/right z/down z/right
       (gather-fact)
       :docs
       common/join-nodes)
  => \"\"Sample test program\"\\n  (+ 1 1) => 2\\n  (long? 3) => true\""
  {:added "0.1"}
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
