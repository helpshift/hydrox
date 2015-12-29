(ns hydrox.analyse-test
  (:use midje.sweet)
  (:require [hydrox.analyse :refer :all]
            [hydrox.core :as hydrox]
            [hydrox.common.util :as util]
            [clojure.java.io :as io]))

(defmacro contains-in [m]
  (cond (map? m)
        `(contains ~(reduce-kv (fn [out k v]
                                 (assoc out k `(contains-in ~v)))
                               {}
                               m))
        :else m))

(def user-dir (System/getProperty "user.dir"))

^{:refer hydrox.analyse/canonical :added "0.1"}
(fact "returns the canonical system path"

  (canonical "src")
  => (str (System/getProperty "user.dir") "/src"))

^{:refer hydrox.analyse/file-type :added "0.1"}
(fact "returns the file-type for entries"

  (file-type {:source-paths ["src"]
              :test-paths   ["test"]} (io/file "src/code.clj"))
  => :source)

^{:refer hydrox.analyse/add-file :added "0.1"}
(fact "adds a file to the folio"
  (-> {:project (util/read-project (io/file "example/project.clj"))}
      (add-file (io/file "example/test/example/core_test.clj"))
      (add-file (io/file "example/src/example/core.clj"))
      (dissoc :project))
  => (contains-in
      {:registry {(str user-dir "/example/test/example/core_test.clj")
                  {'example.core
                   {'foo {:docs vector?, :meta {:added "0.1"}}}},
                  (str user-dir "/example/src/example/core.clj")
                  {'example.core
                   {'foo {:source "(defn foo\n  [x]\n  (println x \"Hello, World!\"))"}}}},
       :references {'example.core
                    {'foo {:docs vector?, :meta {:added "0.1"},
                           :source "(defn foo\n  [x]\n  (println x \"Hello, World!\"))"}}},
       :namespace-lu {'example.core (str user-dir "/example/src/example/core.clj")}}))

^{:refer hydrox.analyse/remove-file :added "0.1"}
(fact "removes a file to the folio"
  (-> {:project (util/read-project (io/file "example/project.clj"))}
      (add-file (io/file "example/src/example/core.clj"))
      (remove-file (io/file "example/src/example/core.clj"))
      (dissoc :project))
  => {:registry {}
      :references {}
      :namespace-lu {}})
