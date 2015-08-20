(ns hydrox.core.regulator-test
  (:use midje.sweet)
  (:require [hydrox.core.regulator :refer :all]
            [clojure.java.io :as io]))

^{:refer hydrox.core.regulator/create-folio :added "0.1"}
(fact "creates the folio for storing all the documentation information")

^{:refer hydrox.core.regulator/mount-folio :added "0.1"}
(fact "adds a watcher to update function/test definitions when files in the project changes")

^{:refer hydrox.core.regulator/unmount-folio :added "0.1"}
(fact "removes the file-change watcher")

^{:refer hydrox.core.regulator/init-folio :added "0.1"}
(fact "runs through all the files and adds function/test definitions to the project")

^{:refer hydrox.core.regulator/start-regulator :added "0.1"}
(fact "starts the regulator")

^{:refer hydrox.core.regulator/stop-regulator :added "0.1"}
(fact "stops the regulator")

^{:refer hydrox.core.regulator/regulator :added "0.1"}
(fact "creates a blank regulator, does not work")
