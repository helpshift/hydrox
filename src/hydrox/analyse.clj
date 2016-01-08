(ns hydrox.analyse
  (:require [hydrox.analyse
             [common :as common]
             test source]
            [hydrox.doc :as doc]
            [hydrox.common
             [data :as data]
             [util :as util]]
            [clojure.java.io :as io]
            [hara.data.diff :as diff])
  (:import java.io.File))

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
  "returns the file-type for entries
 
   (file-type {:source-paths [\"src\"]
               :test-paths   [\"test\"]} (io/file \"src/code.clj\"))
   => :source"
  {:added "0.1"}
  [project ^File file]
  (let [path (.getCanonicalPath file)]
    (or (->> access-paths
             (keep (fn [[v res]]
                     (if (some (fn [^String x] (<= 0 (.indexOf path x)))
                               (get-in project v))
                       res)))
             (first))

        (if (= path (str (:root project) "/" "project.clj"))
          :project)

        (do (println "IGNORE" file)
            :ignore))))

(defn add-code
  [{:keys [project] :as folio} ^File file type]
  (let [fkey     (.getCanonicalPath file)
        registry (get-in folio [:registry fkey])
        result   (common/analyse-file type file project)
        diff     (diff/diff result registry)
        folio    (-> folio
                     (assoc-in  [:registry fkey] result)
                     (update-in [:references] diff/patch diff))
        plus     (concat (-> diff :+ keys) (-> diff :> keys))
        minus    (-> diff :- keys)    ]
    (if (not-empty plus)  (println "Associating:" plus))
    (if (not-empty minus) (println "Deleting:" minus))
    (if (= type :source)
      (assoc-in folio [:namespace-lu (first (keys result))] fkey)
      folio)))

(defn add-documentation [{:keys [project] :as folio} file]
  (if (not (:initialisation folio))
    (let [inputs (-> project :documentation :files)
          ridx (reduce-kv (fn [out k v]
                            (assoc out (:input v) k))
                          {} inputs)
          choice (->> (keys ridx)
                      (filter (fn [suffix] (.endsWith (str file) suffix)))
                      (first))]
      (if (and choice (not (:manual folio)))
        (doc/render-single folio (get ridx choice)))))
  folio)

(defn add-project [{:keys [project] :as folio} file]
  (let [folio (-> file
                  (util/read-project)
                  (select-keys [:version :documentation])
                  (->> (update-in folio [:project] merge)))]
    (println "Project Updated")
    (if-not (:manual folio)
      (doc/render-all folio))
    folio))

(defn add-file
  "adds a file to the folio
   (-> {:project (util/read-project (io/file \"example/project.clj\"))}
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
  [{:keys [project] :as folio} file]
  (let [type (file-type project file)]
    (println "FILE:" (.getName file))
    (cond (#{:source :test} type)
          (add-code folio file type)
          
          (= :project type)
          (add-project folio file)

          (= :doc type)    
          (add-documentation folio file)

          :else
          folio)))

(defn remove-file
  "removes a file to the folio
   (-> {:project (util/read-project (io/file \"example/project.clj\"))}
       (add-file (io/file \"example/src/example/core.clj\"))
       (remove-file (io/file \"example/src/example/core.clj\"))
       (dissoc :project))
   => {:registry {}
       :references {}
       :namespace-lu {}}"
  {:added "0.1"}
  [folio ^File file]
  (let [{:keys [project]} folio
        type (file-type project file)]
    (println "\nRemoving" file)

    (cond (#{:source :test} type)
          (let [fkey     (.getCanonicalPath file)
                registry (get-in folio [:registry fkey])
                diff     (diff/diff {} registry)
                folio    (-> folio
                             (update-in [:registry] dissoc  fkey)
                             (update-in [:references] diff/patch diff))]
            (println "Deleting:"  (-> diff :- keys)) 
            (if (= type :source)
              (update-in folio [:namespace-lu]
                         (fn [m] (reduce-kv (fn [out k v]
                                              (if (= v fkey)
                                                out
                                                (assoc out k v)))
                                           {}
                                           m)))
              folio))

          :else folio)))
