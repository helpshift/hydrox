(ns hydrox.core
  (:require [hydrox.meta.util :as util]
            [hydrox.meta :as meta]
            [hydrox.analyse :as analyser]
            [hydrox.common.data :as data]
            [hara.common.watch :as watch]
            [hara.event :as event]
            [hara.component :as component]
            [hara.io.watch]
            [hara.data.diff :as diff]
            [clojure.java.io :as io])
  (:refer-clojure :exclude [import]))

(defonce ^:dynamic *running* #{})

(defn read-project
  "like `leiningen.core.project/read` but with less features'
   
   (keys (read-project (io/file \"example/project.clj\")))
   => (just [:description :license :name :source-paths :test-paths
             :documentation :root :url :version :dependencies] :in-any-order)"
  {:added "0.1"}
  ([] (read-project (io/file "project.clj")))
  ([file]
   (let [path  (.getCanonicalPath file)
         root  (subs path 0 (- (count path) 12))
         pform (read-string (slurp file))
         [_ name version] (take 3 pform)
         proj  (->> (drop 3 pform)
                    (concat [:name name
                             :version version
                             :root root])
                    (apply hash-map))]
     (-> proj
         (update-in [:source-paths] (fnil identity ["src"]))
         (update-in [:test-paths] (fnil identity ["test"]))))))

(defn create-folio
  "creates the folio for storing all the documentation information"
  {:added "0.1"}
  [{:keys [root] :as project}]
  (data/folio {:meta        {}
               :articles    {}
               :namespaces  {}
               :project     project
               :references  (data/references)
               :registry    (data/registry)
               :root        root}))

(defn mount-folio
  "adds a watcher to update function/test definitions when files in the project changes"
  {:added "0.1"}
  [state {:keys [project root] :as folio}]
  (let [{:keys [source-paths test-paths]} project]
    (watch/add (io/as-file root) :hydrox
               (fn [_ _ _ [type file]]
                 (println "FILE CHANGE DETECTED:" type file)
                 (case type
                   :create (swap! state analyser/add-file file)
                   :modify (swap! state analyser/add-file file)
                   :delete (swap! state analyser/remove-file file)))
               {:filter  [".clj"]
                :recursive true
                :include (concat source-paths test-paths)})
    folio))

(defn unmount-folio
  "removes the file-change watcher"
  {:added "0.1"}
  [folio]
  (watch/remove (io/as-file (:root folio)) :hydrox))

(defn init-folio
  "runs through all the files and adds function/test definitions to the project"
  {:added "0.1"}
  [{:keys [project] :as folio}]
  (reduce (fn [folio file]
            (analyser/add-file folio file))
          folio
          (concat (util/all-files project :source-paths ".clj")
                  (util/all-files project :test-paths ".clj"))))

(defn start-regulator
  "starts the regulator"
  {:added "0.1"}
  [{:keys [project state] :as obj}]
  (let [folio (-> (create-folio project)
                  (init-folio))]
    (mount-folio state folio)
    (reset! state folio)
    (event/signal [:log {:msg (str "Regulator for " (:name project) " started.")}])
    (alter-var-root #'*running* (fn [s] (conj s obj)))
    obj))

(defn stop-regulator
  "stops the regulator"
  {:added "0.1"}
  [{:keys [project state] :as obj}]
  (unmount-folio @state)
  (reset! state nil)
  (event/signal [:log {:msg (str "Regulator for " (:name project) " stopped.")}])
  (alter-var-root #'*running* (fn [s] (disj s obj)))
  obj)

(defrecord Regulator [state project]

  component/IComponent
  (-start [obj] (start-regulator obj))
  (-stop  [obj] (stop-regulator obj))

  (-stopped? [obj]
    (nil? @state)))

(defn regulator
  "creates a blank regulator, does not work"
  {:added "0.1"}
  ([]
   (regulator (read-project)))
  ([project]
   (Regulator. (atom nil) project))
  ([state project]
   (Regulator. state project)))

(defn create-regulator
  "returns a working regulator for a given project file"
  {:added "0.1"}
  [path]
  (let [proj  (read-project (io/file path))
        folio (-> proj
                  (create-folio)
                  (init-folio))
        state (atom folio)]
    (regulator state proj)))

(defn import-docstring
  "imports docstrings given a regulator"
  {:added "0.1"}
  ([] (mapv #(import-docstring % :all) *running*))
  ([reg] (import-docstring reg :all))
  ([reg ns] (import-docstring reg ns nil))
  ([{:keys [state project] :as reg} ns var]
   (let [{:keys [references]
          lu :namespace-lu} @state]
     (cond (= ns :all)
           (doall (meta/import-project project references))

           :else
           (if-let [file (get lu ns)]
             (if var
               (meta/import-var file var references)
               (meta/import-file file references)))))))

(defn purge-docstring
  "purges docstrings given a regulator"
  {:added "0.1"}
  ([] (mapv #(purge-docstring % :all) *running*))
  ([reg] (purge-docstring reg :all))
  ([reg ns] (purge-docstring reg ns nil))
  ([{:keys [state project] :as reg} ns var]
   (let [{lu :namespace-lu} @state]
     (cond (= ns :all)
           (doall (meta/purge-project project))

           :else
           (if-let [file (get lu ns)]
             (if var
               (meta/purge-var file var)
               (meta/purge-file file)))))))

(defn dive
  "starts a dive"
  {:added "0.1"}
  ([] (dive "project.clj"))
  ([path]
   (component/start (regulator (read-project (io/file path))))))

(defn surface
  "finishes a dive"
  {:added "0.1"}
  ([] (doseq [reg *running*]
        (surface reg)))
  ([regulator]
   (component/stop regulator)))
