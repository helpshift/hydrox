(ns nitrox.core
  (:require [leiningen.core.project :as project]
            [hara.common.watch :as watch]
            [hara.io.watch]))

(defn initialise
  ([]
   (init (project/read "project.clj")))
  ([{:keys [root src-paths test-paths] :as project}]
   (let [])))