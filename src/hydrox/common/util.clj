(ns hydrox.common.util)

(defn full-path [path base project]
  (str (:root project) "/" base "/" path))

(defn filter-strings [m]
  (reduce-kv (fn [m k v] (if (string? v)
                           (assoc m k v)
                           m))
             {} m))
