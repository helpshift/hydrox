(ns leiningen.hydrox
  (:require [hydrox.core :as hydrox]
            [hydrox.meta :as meta]
            [hydrox.common.util :as util]
            [leiningen.hydrox.init :as init]))


(defn hydrox
  "
   metadata and documentation management:

   usage:
     lein hydrox (watch)  - default, watches project for changes and updates documentation accordingly
     lein hydrox init     - initialises a project scaffold
     lein hydrox docs     - generates documentation from project
     lein hydrox import   - imports docstrings from test files
     lein hydrox purge    - purges docstrings from code
  "
  [project & args]
  (case (first args)
    nil       (hydrox project "watch")
    "init"    (init/init project)
    "watch"   (do (hydrox/dive)
                  (hydrox/generate-docs)
                  (Thread/sleep 10000000000))
    "docs"    (hydrox/generate-docs (hydrox/single-use))
    "import"  (hydrox/import-docstring (hydrox/single-use))
    "purge"   (meta/purge-project (util/read-project))
    "help"    (println (:doc (meta #'hydrox)))
    (hydrox project "help")))

(comment

  (require '[leiningen.core.project :as project])

  (def project (project/read))
  (hydrox project "init")
  (hydrox project "docs")
  
  
  )
