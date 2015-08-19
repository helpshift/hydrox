(ns hydrox.doc.link.tags-test
  (:use midje.sweet)
  (:require [hydrox.doc.link.tags :refer :all]))

^{:refer hydrox.doc.link.tags/inc-candidate :added "0.1"}
(fact "creates an incremental version of a name"
  
  (inc-candidate "hello") => "hello-0"
  (inc-candidate "hello-1") => "hello-2")

^{:refer hydrox.doc.link.tags/tag-string :added "0.1"}
(fact "creates a string that can be used as an anchor"
  
  (tag-string "hello.world/again")
  => "hello-world--again")

^{:refer hydrox.doc.link.tags/create-candidate :added "0.1"}
(fact "creates a candidate tag from a variety of sources"

  (create-candidate {:origin :ns :ns 'clojure.core})
  => "ns-clojure-core"

  (create-candidate {:title "hello again"})
  => "hello-again"

  (create-candidate {:type :image :src "http://github.com/hello/gather.jpeg"})
  => "img-http----github-com--hello--gather-jpeg")


^{:refer hydrox.doc.link.tags/create-tag :added "0.1"}
(fact "creates a tag from an element"

  (let [tags (atom #{})
        result (create-tag {:title "hello"} tags)]
    [@tags result])
  => [#{"hello"} {:title "hello", :tag "hello"}]

  (let [tags (atom #{"hello"})
        result (create-tag {:title "hello"} tags)]
    [@tags result])
  => [#{"hello" "hello-0"} {:title "hello", :tag "hello-0"}])
