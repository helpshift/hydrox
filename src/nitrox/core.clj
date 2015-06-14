(ns nitrox.core
  (:require [leiningen.core.project :as project]))

(defn dive
  ([]
   (dive (project/read "project.clj")))
  ([{:keys [root src-paths test-paths] :as project}]
   (let [])))