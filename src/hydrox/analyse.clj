(ns hydrox.analyse
  (:require [hydrox.analyse
             [common :as common]
             test source]
            [hydrox.common.data :as data]
            [clojure.java.io :as io]
            [hara.data.diff :as diff]))

(def access-paths
  [[[:source-paths]         :source]
   [[:documentation :paths] :doc]
   [[:test-paths]           :test]])

(defn canonical
  "returns the canonical system path
   
   (canonical \"src\")
   => (str (System/getProperty \"user.dir\") \"/src\")"
  {:added "0.1"}
  [path]
  (.getCanonicalPath (io/as-file path)))

(defn file-type
  "returns the file-type for a particular file
 
   (file-type {:source-paths [\"src\"]
               :test-paths   [\"test\"]} (io/file \"src/code.clj\"))
   => :source"
  {:added "0.1"}
  [project file]
  (let [path (.getCanonicalPath file)]
    (or (->> access-paths
             (keep (fn [[v res]]
                     (if (some (fn [x] (<= 0 (.indexOf path x)))
                               (get-in project v))
                       res)))
             (first))
        :ignore)))

(defn add-file
  "adds a file to the folio
   (-> {:project (hydrox/read-project (io/file \"example/project.clj\"))}
       (add-file (io/file \"example/test/example/core_test.clj\"))
       (add-file (io/file \"example/src/example/core.clj\"))
       (dissoc :project))
   => (contains-in
       {:registry {(str user-dir \"/example/test/example/core_test.clj\")
                   {'example.core
                   {'foo {:docs vector?, :meta {:added \"0.1\"}}}},
                   (str user-dir \"/example/src/example/core.clj\")
                   {'example.core
                    {'foo {:source \"(defn foo\\n  [x]\\n  (println x \\\"Hello, World!\\\"))\"}}}},
        :references {'example.core
                     {'foo {:docs vector?, :meta {:added \"0.1\"},
                            :source \"(defn foo\\n  [x]\\n  (println x \\\"Hello, World!\\\"))\"}}},
        :namespace-lu {'example.core (str user-dir \"/example/src/example/core.clj\")}})"
  {:added "0.1"}
  [folio file]
  (let [{:keys [project]} folio
        type (file-type project file)]
    (println "\nProcessing" file)

    (cond (#{:source :test} type)
          (let [fkey     (.getCanonicalPath file)
                registry (get-in folio [:registry fkey])
                result   (common/analyse-file type file project)
                diff     (diff/diff result registry)
                _        (do (println "Associating:" (concat (-> diff :+ keys) (-> diff :> keys)))
                             (println "Deleting:"    (concat (-> diff :- keys))))
                folio    (-> folio
                             (assoc-in  [:registry fkey] result)
                             (update-in [:references] diff/patch diff))]
            (if (= type :source)
              (assoc-in folio [:namespace-lu (first (keys result))] fkey)
              folio))

          :else folio)))

(defn remove-file
  "removes a file to the folio
   (-> {:project (hydrox/read-project (io/file \"example/project.clj\"))}
       (add-file (io/file \"example/src/example/core.clj\"))
       (remove-file (io/file \"example/src/example/core.clj\"))
       (dissoc :project))
   => {:registry {}
       :references {}
       :namespace-lu {}}"
  {:added "0.1"}
  [folio file]
  (let [{:keys [project]} folio
        type (file-type project file)]
    (println "REMOVING (TODO)" type file)
    folio))
