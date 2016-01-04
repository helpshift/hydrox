(ns hydrox.core-test
  (:use midje.sweet)
  (:require [hydrox.core :refer :all]
            [hydrox.analyse :as analyser]
            [hydrox.common.util :as util]
            [hydrox.core.regulator :as regulator]
            [hara.component :as component]
            [clojure.java.io :as io]))

^{:refer hydrox.core/submerged? :added "0.1"}
(fact "checks if dive has started")

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

  (def reg (let [proj   (util/read-project)
                 folio (-> proj
                           (create-folio)
                           (init-folio))
                 state (atom folio)]
             (Regulator. state proj)))

  (def reg (let [proj  (-> (util/read-project)
                           (assoc :initialise false))
                 folio (-> proj
                           (regulator/create-folio)
                           (analyser/add-file (io/file "src/hydrox/core/regulator.clj")))
                 state (atom folio)]
             (regulator/regulator state proj)))

  (-> reg :state deref :sink deref)
 
  (component/start reg)

  (dive)
  (surface)
  (generate-docs)
  
  (async/go
    (let [d (async/<! (:channel @(:state reg)))]
      (println d)))

  (-> regulator/*running*
      first
      :state
      deref
      :project
      :version)

  
  
  (:references @(:state reg))

  (import-docstring (once-off "project.clj"))

  (import-docstring reg 'hydrox.doc.structure)
  (purge-docstring reg 'hydrox.analyse.test)

  @(:state reg)
  (:project reg)
  (.getParent(io/file "project.clj")))
