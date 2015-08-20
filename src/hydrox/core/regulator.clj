(ns hydrox.core.regulator
  (:require [hydrox.meta.util :as util]
            [hydrox.meta :as meta]
            [hydrox.analyse :as analyser]
            [hydrox.common.data :as data]
            [hara.common.watch :as watch]
            [hara.event :as event]
            [hara.component :as component]
            [hara.io.watch]
            [clojure.java.io :as io]))

(defonce ^:dynamic *running* #{})

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
  ([project]
   (Regulator. (atom nil) project))
  ([state project]
   (Regulator. state project)))
 
