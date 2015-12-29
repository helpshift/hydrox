(ns hydrox.common.util
  (:require [clojure.java.io :as io]))

(defn full-path
  "constructs a path from a project
 
   (full-path \"example/file.clj\" \"src\" {:root \"/home/user\"})
   => \"/home/user/src/example/file.clj\""
  {:added "0.1"} [path base project]
  (str (:root project) "/" base "/" path))

(defn filter-pred
  "filters values of a map that fits the predicate
   (filter-pred string? {:a \"valid\" :b 0})
   => {:a \"valid\"}"
  {:added "0.1"} [pred? m]
  (reduce-kv (fn [m k v] (if (pred? v)
                           (assoc m k v)
                           m))
             {} m))

(defn escape-dollars
  "for regex purposes, escape dollar signs in templates"
  {:added "0.1"}
  [s]
  (.replaceAll s "\\$" "\\\\\\$"))

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
