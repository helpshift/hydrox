(ns hydrox.meta
  (:require [hydrox.meta.util :as util]
            [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]))

(defn selector
  "builds a selector for functions
   
   (selector 'hello)
   => '[(#{defn defmulti} | hello ^:%?- string? ^:%?- map? & _)]"
  {:added "0.1"}
  ([] (selector nil))
  ([var]
   [(list '#{defn defmulti} '| (or var '_) '^:%?- string? '^:%?- map? '& '_)]))

(defn edit-file
  "helper function for file manipulation used by import and purge"
  {:added "0.1"}
  [file var references edit-fn]
  (let [zloc (source/of-file file)
        nsp  (-> (query/$ zloc [(ns | _ & _)] {:walk :top})
                 first)
        edit (edit-fn nsp references)
        zloc (-> zloc
                 (query/modify (selector var)
                               edit
                               {:walk :top}))]
    (util/write-to-file zloc file)))

(defn import-fn [nsp references]
  (fn [zloc]
    (util/import-location zloc nsp references)))

(defn import-var
  [file var references]
  (edit-file file var references import-fn))

(defn import-file
  [file references]
  (edit-file file nil references import-fn))

(defn import-project [project references]
  (for [file (util/all-files project :source-paths ".clj")]
    (import-file file references)))

(defn purge-fn [nsp references]
  identity)

(defn purge-var
  [file var]
  (edit-file file var nil purge-fn))

(defn purge-file [file]
  (edit-file file nil nil purge-fn))

(defn purge-project [project]
  (for [file (util/all-files project :source-paths ".clj")]
    (purge-file file)))

(comment

  (purge-file "src/hydrox/code.clj")

  (import-file "src/hydrox/code.clj"
               {'hydrox.meta {'import-var {:docs [(node/string-node "Hello there")]
                                           :meta {:added "0.1"}}}})
  (import-var "src/hydrox/code.clj"
              'import-var
              {'hydrox.meta {'import-var {:docs [(node/string-node "Hello there")]
                                          :meta {:added "0.1"}}}})

  (def z (source/of-file "src/hydrox/code.clj")))
