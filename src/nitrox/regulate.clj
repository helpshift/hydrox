(ns nitrox.regulate
  (:require [leiningen.core.project :as project]
            [hara.common.watch :as watch]
            [hara.io.watch]))

(defn start
  ([]
   (init (project/read "project.clj")))
  ([{:keys [root src-paths test-paths] :as project}]
   (let [])))
   
