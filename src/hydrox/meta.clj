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

(defn import-fn
  "helper function for file import"
  {:added "0.1"}
  [nsp references]
  (fn [zloc]
    (util/import-location zloc nsp references)))

(defn import-var
  "import docs for a single var"
  {:added "0.1"}
  [file var references]
  (edit-file file var references import-fn))

(defn import-file
  "import docs for a file"
  {:added "0.1"}
  [file references]
  (edit-file file nil references import-fn))

(defn import-project
  "import docs for the entire project"
  {:added "0.1"}
  [project references]
  (for [file (util/all-files project :source-paths ".clj")]
    (import-file file references)))

(defn purge-fn
  "helper function for file purge"
  {:added "0.1"}
  [nsp references]
  identity)

(defn purge-var
  "purge docs for a single var"
  {:added "0.1"}
  [file var]
  (edit-file file var nil purge-fn))

(defn purge-file
  "purge docs for a file"
  {:added "0.1"}
  [file]
  (edit-file file nil nil purge-fn))

(defn purge-project
  "purge docs for the entire project"
  {:added "0.1"}
  [project]
  (for [file (util/all-files project :source-paths ".clj")]
    (purge-file file)))
