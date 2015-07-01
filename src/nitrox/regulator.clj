(ns nitrox.regulator
  (:require [nitrox.code.util :as util]
            [nitrox.code :as code]
            [nitrox.analyser :as analyser]
            [nitrox.common.data :as data]
            [hara.common.watch :as watch]
            [hara.event :as event]
            [hara.component :as component]
            [hara.io.watch]
            [hara.data.diff :as diff]
            [clojure.java.io :as io])
  (:refer-clojure :exclude [import]))

(def ^:dynamic *running* #{})

(defn read-project
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

(defn create-folio [{:keys [root] :as project}]
  (data/folio {:meta        {}
               :articles    {}
               :namespaces  {}
               :project     project
               :references  (data/references)
               :registry    (data/registry)
               :root        root}))

(defn mount-folio [state {:keys [project root] :as folio}]
  (let [{:keys [source-paths test-paths]} project]
    (watch/add (io/as-file root) :nitrox
               (fn [_ _ _ [type file]]
                 (case type
                   :create (swap! state analyser/add-file file)
                   :modify (swap! state analyser/add-file file)
                   :delete (swap! state analyser/remove-file file)))
               {:filter  [".clj"]
                :include (->> (concat source-paths test-paths)
                              (map #(subs % (-> root count inc))))
                :async true})
    folio))

(defn unmount-folio [folio] 
  (watch/remove (io/as-file (:root folio)) :documentation))

(defn init-folio [{:keys [project] :as folio}]
  (reduce (fn [folio file]
            (analyser/add-file folio file))
          folio
          (concat (util/all-files project :source-paths ".clj")
                  #_(util/all-files project :test-paths ".clj"))))

(defrecord Regulator [state project]

  component/IComponent
  (-start [obj]
    (let [folio (-> (create-folio project)
                    (init-folio))]
      (mount-folio state folio)
      (reset! state folio)
      (event/signal [:log {:msg (str "Regulator for " (:name project) " started.")}])
      (alter-var-root #'*running* (fn [s] (conj s obj)))      
      obj))
  
  (-stop  [obj]
    (unmount-folio @state)
    (reset! state nil)
    (event/signal [:log {:msg (str "Regulator for " (:name project) " stopped.")}])
    (alter-var-root #'*running* (fn [s] (disj s obj)))      
    obj)

  (-stopped? [obj]
    (nil? @state)))

(defn regulator
  ([]
   (regulator (read-project)))
  ([project]
   (Regulator. (atom nil) project)))

(defn import-docstring
  ([reg] (import-docstring reg :all))
  ([reg ns] (import-docstring reg ns nil))
  ([{:keys [state project] :as reg} ns var]
   (let [{:keys [references]
          lu :namespace-lu} @state]
     (cond (= ns :all)
           (code/import-project project references)

           :else
           (if-let [file (get lu ns)]
             (if var
               (code/import-var file var references)
               (code/import-file file references)))))))

(defn import [& args]
  (doseq [reg *running*]
    (apply import-docstring reg args)))

(defn purge-docstring
  ([reg] (purge-docstring reg :all))
  ([reg ns] (purge-docstring reg ns nil))
  ([{:keys [state project] :as reg} ns var]
   (let [{lu :namespace-lu} @state]
     (cond (= ns :all)
           (code/purge-project project)

           :else
           (if-let [file (get lu ns)]
             (if var
               (code/purge-var file var)
               (code/purge-file file)))))))

(defn purge [& args]
  (doseq [reg *running*]
    (apply purge-docstring reg args)))

(comment
  (def reg (let [proj  (read-project)
                 folio (-> proj 
                           (create-folio)
                           (init-folio))
                 state (atom folio)]
             (Regulator. state proj)))

  (def reg (let [proj  (read-project)
                 folio (-> proj 
                           (create-folio)
                           (analyser/add-file (io/file "src/nitrox/analyser/test.clj"))
                           (analyser/add-file (io/file "test/nitrox/analyser/test_test.clj")))
                 state (atom folio)]
             (Regulator. state proj)))

  (:references @(:state reg))
  
  
  (import-docstring reg 'nitrox.analyser.test)
  (purge-docstring reg 'nitrox.analyser.test)
  
  @(:state reg)
  (:project reg)
  (.getParent(io/file "project.clj")))


