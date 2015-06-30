(ns nitrox.analyser.source
  (:require [rewrite-clj.zip :as source]
            [jai.query :as query]
            [nitrox.analyser.common :as common]))

(defmethod common/analyse-file :source [_ file opts]
  "collects the source code for a particular file"
  {:added "0.2"}
  [file]
  (let [zloc (source/of-file file)
        nsp  (->  (query/$ zloc [(ns | _ & _)] {:walk :top})
                  first)
        fns  (->> (query/$ zloc [(#{defn defmulti} | _ ^:%?- string? ^:%?- map? & _)]
                           {:return :zipper :walk :top})
                  (map (juxt source/sexpr
                             (comp #(hash-map :source %)
                                   source/string
                                   source/up)))
                  (into {}))]
    {nsp fns}))
