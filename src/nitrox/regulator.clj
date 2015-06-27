(ns nitrox.regulator
  (:require [leiningen.core.project :as project]
            [nitrox.code.util :as util]
            [nitrox.analyser :as analyser]
            [nitrox.common.data :as data]
            [hara.common.watch :as watch]
            [hara.io.watch]
            [hara.data.diff :as diff]
            [clojure.java.io :as io]))

(def access-paths
  [[[:source-paths]         :source]
   [[:documentation :paths] :doc]
   [[:test-paths]           :test]])

(defn canonical [path]
  (.getCanonicalPath (io/as-file path)))

(defn file-type [project file]
  (let [path (.getCanonicalPath file)]
    (or (->> access-paths
             (keep (fn [[v res]]
                     (if (some (fn [x] (<= 0 (.indexOf path x)))
                               (get-in project v))
                       res)))
             (first))
        :ignore)))

(defn create-folio [global {:keys [root] :as project}]
  (data/folio {:global global
               :root root
               :project project
               :reference (data/reference)
               :registry  (data/registry)}))

(defn mount-folio [{:keys [global project root] :as folio}]
  (let [{:keys [source-paths test-paths]} project]
    (watch/add (io/as-file root) :documentation
               (fn [_ _ _ [type file]]
                 (case type
                   :create (swap! global update-in [root] analyser/add-file file)
                   :modify (swap! global update-in [root] analyser/add-file file)
                   :delete (swap! global update-in [root] analyser/remove-file file)))
               {:filter  [".clj"]
                :include (->> (concat source-paths test-paths)
                              (map #(subs % (-> root count inc))))
                :async true})
    (println "FOLIO MOUNTED:" (:root folio))
    folio))

(defn unmount-folio [folio]
  (println "FOLIO UNMOUNTED:" (:root folio))
  (watch/remove (io/as-file (:root folio)) :documentation))


(defn add-project [global {:keys [root] :as project}]
  (when-not (get @global root)
    (let [folio (create-folio global project)
          folio (reduce (fn [folio file]
                          (add-file folio file))
                        folio
                        (util/all-files project :source-paths ".clj"))]
      (swap! global assoc root (mount-folio folio))))
  global)

(defn remove-project [global project]
  (when-let [entry (get @global (:root project))]
    (unmount-folio entry)
    (swap! global dissoc (:root project)))
  global)

(defn start
  ([]
   (init (project/read "project.clj")))
  ([{:keys [root src-paths test-paths] :as project}]
   (let [global (atom {})]
     (add-project global project))))
