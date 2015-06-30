(ns nitrox.analyser
  (:require [nitrox.analyser
             [common :as common]
             test source doc]
            [nitrox.common.data :as data]
            [clojure.java.io :as io]
            [hara.data.diff :as diff]))

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

(defn add-file [folio file]
  (let [{:keys [project]} folio
        type (file-type project file)]
    (println "\nProcessing" file)

    (cond #{:source :type}
          (let [fkey     (.getCanonicalPath file)
                registry (get-in folio [:registry fkey])
                result   (common/analyse-file type file)
                diff     (diff/diff result registry)]
            (println "Associating:" (concat (-> diff :+ keys) (-> diff :> keys)))
            (println "Deleting:"    (concat (-> diff :- keys)))
            (-> folio
                (assoc-in  [:registry fkey] result)
                (update-in [:references] diff/patch diff)))

          :else folio)))

(defn remove-file [folio file]
  (let [{:keys [project]} folio
        type (file-type project file)]
    (println "REMOVING (TODO)" type file)
    folio))
