(ns hydrox.core-test
  (:use midje.sweet)
  (:require [hydrox.core :refer :all]
            [clojure.java.io :as io]))

^{:refer hydrox.core/read-project :added "0.1"}
(fact "like `leiningen.core.project/read` but with less features'"

  (keys (read-project (io/file "example/project.clj")))
  => (just [:description :license :name :source-paths :test-paths
            :documentation :root :url :version :dependencies] :in-any-order))

^{:refer hydrox.core/single-use :added "0.1"}
(fact "returns a working regulator for a given project file")

^{:refer hydrox.core/import-docstring :added "0.1"}
(fact "imports docstrings given a regulator")

^{:refer hydrox.core/purge-docstring :added "0.1"}
(fact "purges docstrings given a regulator")

^{:refer hydrox.core/generate-docs :added "0.1"}
(fact "generates html docs for :documentation entries in project.clj")

^{:refer hydrox.core/dive :added "0.1"}
(fact "starts a dive")

^{:refer hydrox.core/surface :added "0.1"}
(fact "finishes a dive")


(comment
  (event/deflistener log-listener
    :log
    m
    (println m))

  (import-docstring)

  (dive)
  (surface)
  (purge-docstring)
  (import-docstring)
  *running*
  (watch/add (io/file (:root (read-project))) :small
             (fn [_ _ _ v]
               (println v)))
  oeu
  (watch/list (io/file (:root (read-project))))
  (watch/remove (io/file (:root (read-project))) :hydrox)
  (doseq [reg *running*]
    (println reg)
    (import-docstring reg :all))
  (mapv #(import-docstring % :all) *running*)
  )

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
