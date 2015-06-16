(ns nitrox.code
  (:require [nitrox.code.util :as util]
            [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]))

(defn selector
  ([] (selector nil))
  ([var]
   [(list '#{defn defmulti} '| (or var '_) '^:%?- string? '^:%?- map? '& '_)]))

(defn edit-file
  [file var reference edit-fn]
  (let [zloc (source/of-file file)
        nsp  (-> (query/$ zloc [(ns | _ & _)] {:walk :top})
                 first)
        edit (edit-fn nsp reference)
        zloc (-> zloc
                 (query/modify (selector var)
                               edit
                               {:walk :top}))]
    (util/write-to-file zloc file)))

(defn import-fn [nsp reference]
  (fn [zloc]
    (util/import-location zloc nsp reference)))

(defn import-var
  [file var reference]
  (edit-file file var reference import-fn))

(defn import-file
  [file reference]
  (edit-file file nil reference import-fn))

(defn import-project [project reference]
  (for [file (util/all-files project :source-paths ".clj")]
    (import-file file reference)))

(defn purge-fn [nsp reference]
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

  (purge-file "src/nitrox/code.clj")

  (import-file "src/nitrox/code.clj"
               {'nitrox.code {'import-var {:docs [(node/string-node "Hello there")]
                                           :meta {:added "0.1"}}}})
  (import-var "src/nitrox/code.clj"
              'import-var
              {'nitrox.code {'import-var {:docs [(node/string-node "Hello there")]
                                          :meta {:added "0.1"}}}})

  (def z (source/of-file "src/nitrox/code.clj"))

  ()
        
  
  )
