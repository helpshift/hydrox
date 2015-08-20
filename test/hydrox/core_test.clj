(ns hydrox.core-test
  (:use midje.sweet)
  (:require [hydrox.core :refer :all]
            [clojure.java.io :as io]))

^{:refer hydrox.core/read-project :added "0.1"}
(fact "like `leiningen.core.project/read` but with less features'"
  
  (keys (read-project (io/file "example/project.clj")))
  => (just [:description :license :name :source-paths :test-paths
            :documentation :root :url :version :dependencies] :in-any-order))

^{:refer hydrox.core/create-folio :added "0.1"}
(fact "creates the folio for storing all the documentation information")

^{:refer hydrox.core/mount-folio :added "0.1"}
(fact "adds a watcher to update function/test definitions when files in the project changes")

^{:refer hydrox.core/unmount-folio :added "0.1"}
(fact "removes the file-change watcher")

^{:refer hydrox.core/init-folio :added "0.1"}
(fact "runs through all the files and adds function/test definitions to the project")

^{:refer hydrox.core/start-regulator :added "0.1"}
(fact "starts the regulator")

^{:refer hydrox.core/stop-regulator :added "0.1"}
(fact "stops the regulator")

^{:refer hydrox.core/regulator :added "0.1"}
(fact "creates a blank regulator, does not work")

^{:refer hydrox.core/create-regulator :added "0.1"}
(fact "returns a working regulator for a given project file")

^{:refer hydrox.core/import-docstring :added "0.1"}
(fact "imports docstrings given a regulator")

^{:refer hydrox.core/purge-docstring :added "0.1"}
(fact "purges docstrings given a regulator")

^{:refer hydrox.core/dive :added "0.1"}
(fact "starts a dive")

^{:refer hydrox.core/surface :added "0.1"}
(fact "finishes a dive")


(comment
  (component/start (regulator (read-project (io/file "../hara/project.clj"))))

  (def reg (let [proj  (read-project)
                 folio (-> proj
                           (create-folio)
                           (init-folio))
                 state (atom folio)]
             (Regulator. state proj)))

  (def reg (let [proj  (read-project)
                 folio (-> proj
                           (create-folio)
                           (analyser/add-file (io/file "src/hydrox/analyser/test.clj"))
                           (analyser/add-file (io/file "test/hydrox/analyser/test_test.clj")))
                 state (atom folio)]
             (Regulator. state proj)))

  (:references @(:state reg))

  (import-docstring (once-off "project.clj"))

  (import-docstring reg 'hydrox.doc.structure)
  (purge-docstring reg 'hydrox.analyse.test)

  @(:state reg)
  (:project reg)
  (.getParent(io/file "project.clj")))
