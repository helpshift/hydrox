(ns hydrox.analyse.source
  (:require [rewrite-clj.zip :as source]
            [jai.query :as query]
            [hydrox.analyse.common :as common]))

(defn analyse-source-file
  "analyses a source file for namespace and function definitions
   (analyse-source-file \"example/src/example/core.clj\" {})
   => '{example.core
        {foo
         {:source \"(defn foo\\n  [x]\\n  (println x \\\"Hello, World!\\\"))\"}}}"
  {:added "0.1"}
  [file opts]
  (let [zloc (source/of-file file)
        nsp  (->  (query/$ zloc [(ns | _ & _)] {:walk :top})
                  first)
        fns  (->> (query/$ zloc [(#{defn defmulti defmacro} | _ ^:%?- string? ^:%?- map? & _)]
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
