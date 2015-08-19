(ns hydrox.analyse.source
  (:require [rewrite-clj.zip :as source]
            [jai.query :as query]
            [hydrox.analyse.common :as common]))

(defn analyse-source-file
  [file opts]
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

(defmethod common/analyse-file
  :source
  [_ file opts]
  (analyse-source-file file opts))
