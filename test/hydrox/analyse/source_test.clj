(ns hydrox.analyse.source-test
  (:use midje.sweet)
  (:require [hydrox.analyse.source :refer :all]))

^{:refer hydrox.analyse.source/analyse-source-file :added "0.1"}
(fact "analyses a source file for namespace and function definitions"
  (analyse-source-file "example/src/example/core.clj" {})
  => '{example.core
       {foo
        {:source "(defn foo\n  [x]\n  (println x \"Hello, World!\"))"}}})
