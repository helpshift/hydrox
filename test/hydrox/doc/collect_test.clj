(ns hydrox.doc.collect-test
  (:use midje.sweet)
  (:require [hydrox.doc.collect :refer :all]))

^{:refer hydrox.doc.collect/collect-namespaces :added "0.1"}
(fact "combines `:ns-form` directives into a namespace map for easy referral"
  
  (collect-namespaces
   {:articles {"example" {:elements [{:type :ns-form
                                      :ns    'clojure.core}]}}}
   "example")
  => '{:articles {"example" {:elements ()}}
       :namespaces {clojure.core {:type :ns-form :ns clojure.core}}})


^{:refer hydrox.doc.collect/collect-article :added "0.1"}
(fact "shunts `:article` directives into a seperate `:meta` section"
  
  (collect-article
   {:articles {"example" {:elements [{:type :article
                                      :options {:color :light}}]}}}
   "example")
  => '{:articles {"example" {:elements []
                             :meta {:options {:color :light}}}}})

^{:refer hydrox.doc.collect/collect-global :added "0.1"}
(fact "shunts `:global` directives into a globally available `:meta` section"
  
  (collect-global
   {:articles {"example" {:elements [{:type :global
                                      :options {:color :light}}]}}}
   "example")
  => {:articles {"example" {:elements ()}}
      :meta {:options {:color :light}}})

^{:refer hydrox.doc.collect/collect-tags :added "0.1"}
(fact "puts any element with `:tag` attribute into a seperate `:tag` set"
  
  (collect-tags
   {:articles {"example" {:elements [{:type :chapter :tag  "hello"}
                                     {:type :chapter :tag  "world"}]}}}
   "example")
  => {:articles {"example" {:elements [{:type :chapter :tag "hello"}
                                       {:type :chapter :tag "world"}]
                            :tags #{"hello" "world"}}}})

^{:refer hydrox.doc.collect/collect-citations :added "0.1"}
(fact "shunts `:citation` directives into a seperate `:citation` section"
  
  (collect-citations
   {:articles {"example" {:elements [{:type :citation :author "Chris"}]}}}
   "example")
  => {:articles {"example" {:elements [],
                            :citations [{:type :citation, :author "Chris"}]}}})
