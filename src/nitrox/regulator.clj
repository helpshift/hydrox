(ns nitrox.regulator
  (:require ;;[leiningen.core.project :as project]
            [nitrox.code.util :as util]
            [nitrox.analyser :as analyser]
            [nitrox.common.data :as data]
            [hara.common.watch :as watch]
            [hara.event :as event]
            [hara.component :as component]
            [hara.io.watch]
            [hara.data.diff :as diff]
            [clojure.java.io :as io]))

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
    (println "FOLIO MOUNTED:" (:root folio))
    folio))

(defn unmount-folio [folio]
  (println "FOLIO UNMOUNTED:" (:root folio))
  (watch/remove (io/as-file (:root folio)) :documentation))


(defrecord Regulator [state project]

  component/IComponent
  (-start [obj]
    (let [folio (create-folio project)
          folio  (reduce (fn [folio file]
                           (analyser/add-file folio file))
                         folio
                         (concat (util/all-files project :source-paths ".clj")
                                 (util/all-files project :test-paths ".clj")))]
      (reset! state folio)
      (event/signal [:log {:msg (str "Regulator for " (:name project) " started.")}])
      (mount-folio state folio)))
  
  (-stop  [obj]
    (unmount-folio @state)
    (reset! state nil))

  (-stopped? [obj]
    (nil? @state)))

(defn regulator
  ([]
   (regulator nil ;(project/read "project.clj")
              ))
  ([project]
   (Regulator. (atom nil) project)))
