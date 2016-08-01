(ns boot-hydrox
  {:boot/export-tasks true}
  (:require [clojure.pprint :refer [pprint]]
            [boot.core :as core]
            [hydrox.core :as hydrox]
            [hydrox.core.regulator :as hydrox-regulator]
            [hara.component :as component]))

(def ^:private deps
  '[[helpshift/hydrox "0.1.16"]])

(defn- make-regulator
  "returns a working regulator for a project definition"
  [proj]
  (let [folio (-> proj
                  (hydrox-regulator/create-folio)
                  (hydrox-regulator/init-folio))
        state (atom folio)]
    (hydrox-regulator/regulator state proj)))

(defn- make-proj []
  (let [env (core/get-env)
        defaults {:description "unknown"
                  :license "unknown"
                  :name "unknown"
                  :source-paths ["src"]
                  :test-paths ["test"]
                  :documentation {}
                  :root "."
                  :url "unknown"
                  :version "unknown"
                  :dependencies []}
        config {:description ""
                :license ""
                :name ""
                :source-paths (into [] (env :source-paths))
                :test-paths (env :test-paths)
                :documentation (env :documentation)
                :root (env :root)
                :url (env :url)
                :version (env :version)
                :dependencies (env :dependencies)}]
    (merge-with #(or %2 %1) defaults config)))

(core/deftask hydrox
  "hydrox documentation generation"
  []
  (let []
    (core/with-pre-wrap fileset
      (let [namespaces (core/fileset-namespaces fileset)
            ns-names (map name namespaces)
            args (into [] ns-names)
            proj (make-proj)]
        (pprint proj)
        (component/start (make-regulator proj))
        (hydrox/generate-docs)
        (core/commit! fileset)))))
