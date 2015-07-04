(ns nitrox.doc.link-test
  (:use midje.sweet)
  (:require [nitrox.doc.link :refer :all]
            [nitrox.doc.parse :as parse]
            [nitrox.analyser :as analyser]
            [nitrox.regulator :as regulate]
            [clojure.java.io :as io]))

(def folio
  (-> (regulate/read-project (io/file "example/project.clj"))
      (regulate/create-folio)))

(def settings
  (-> folio :project :documentation :files))
