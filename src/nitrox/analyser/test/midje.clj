(ns nitrox.analyser.test.midje
  (:require [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]))

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
                  (conj output
                        (node/string-node (->> (source/sexpr zloc)
                                               (string/split-lines)
                                               (map-indexed (fn [i s]
                                                              (str (if (zero? i) "" "  ")
                                                                   (string/triml s))) )))))
           
           :else
           (recur (source/right* zloc) (conj output (source/node zloc))))))

(defn gather-fact
  "collects information from a fact form"
  [zloc]
  (if (-> zloc source/up source/up source/tag (= :meta))
    (let [mta (-> zloc source/up source/left source/sexpr)
          sym (:refer mta)]
      (if sym
        (assoc mta
               :ns   (symbol (str (.getNamespace sym)))
               :var  (symbol (name sym))
               :docs (gather-fact-body zloc))))))

(defn gather-midje-file
  "collects test and metadata information from a midje test file"
  {:added "0.2"}
  ([file] (gather-midje-file file {}))
  ([file output]
   (let [zloc (source/of-file file)
         nsp  (-> (query/$ zloc [(ns | _ & _)] {:walk :top})
                  first)
         fns  (query/$ zloc [(fact | & _)] {:return :zipper :walk :top})]
     (->> (keep gather-fact fns)
          (reduce (fn [m {:keys [ns var docs] :as meta}]
                    (-> m
                        (assoc-in [ns var :docs] docs)
                        (assoc-in [ns var :meta] (dissoc meta :docs :ns :var :refer))))
                  output)))))
