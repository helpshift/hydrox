(ns hydrox.doc.link-test
  (:use midje.sweet)
  (:require [hydrox.doc.link :refer :all]
            [hydrox.doc.parse :as parse]
            [hydrox.doc :as doc]
            [hydrox.analyse :as analyse]
            [hydrox.core :as core]
            [clojure.java.io :as io]))

(def folio
  (-> (core/read-project (io/file "example/project.clj"))
      (core/create-folio)))

(fact "t1 - test for collect-global"
  
  (:meta (doc/generate-article folio
                               "global"
                               "test/documentation/short/t1_global.clj"))
  => {:type :global, :tracking "UA-31320512-2", :link {:auto-number true, :auto-tag true}})


(fact "t2 - test for collect-article"
  
  (get-in (doc/generate-article folio
                                "article"
                                "test/documentation/short/t2_article.clj")
          [:articles "article" :meta])
  => {:type :article, :tracking "UA-31320512-2", :link {:auto-number true, :auto-tag true}})


(fact "t3 - test for link-namespace"
  
  (-> (doc/generate-article folio
                            "namespaces"
                            "test/documentation/short/t3_namespaces.clj")
      (get-in [:articles "namespaces" :elements])
      first)
  => '{:type :code,
          :refer documentation.short.t3-namespaces
          :ns documentation.short.t3-namespaces
          :origin :ns
          :indentation 0
          :code "(ns documentation.short.t3-namespaces)"
          :tag "ns-documentation-short-t3-namespaces"}

  (-> (doc/generate-article folio
                            "namespaces"
                            "test/documentation/short/t3_namespaces.clj")
      (select-keys [:anchors-lu :anchors]))
  => {:anchors-lu {"namespaces"
                   {:by-tag
                    {"ns-documentation-short-t3-namespaces"
                     {:type :code
                      :tag "ns-documentation-short-t3-namespaces"}}}}
      :anchors     {"namespaces"
                    {"ns-documentation-short-t3-namespaces"
                     {:type :code
                      :tag "ns-documentation-short-t3-namespaces"}}}})

(fact "t4 - test for link-numbers"

  (-> (doc/generate-article folio
                            "numbers"
                            "test/documentation/short/t4_numbers.clj")
      (get-in [:articles "numbers" :elements])
      (#(map :number %)))
  => ["1" "1.1" "1.2" "1.3" "1.4" "2" "3" "4" "5"]

  (-> (doc/generate-article folio
                            "numbers"
                            "test/documentation/short/t4_numbers.clj")
      
      (get-in [:anchors-lu "numbers" :by-number]))
  => {:chapter {"1" {:type :chapter, :tag "an-unexpected-party", :number "1"},
                "2" {:type :chapter, :tag "roast-mutton", :number "2"},
                "3" {:type :chapter, :tag "a-short-rest", :number "3"},
                "4" {:type :chapter, :tag "over-hill-and-under-hill", :number "4"},
                "5" {:type :chapter, :tag "riddles-in-the-dark", :number "5"}},
      :section {"1.1" {:type :section, :tag "in-a-hole-in-the-ground-there-lived-a-hobbit", :number "1.1"},
                "1.2" {:type :section, :tag "what-on-earth-did-i-ask-him-to-tea-for", :number "1.2"},
                "1.3" {:type :section, :tag "i-see-they-have-begun-to-arrive-already", :number "1.3"},
                "1.4" {:type :section, :tag "now-for-some-music", :number "1.4"}}})

(fact "t5 - test for link-references"

  (-> folio
      (analyse/add-file (io/file "example/src/example/core.clj"))
      (doc/generate-article "references"
                            "test/documentation/short/t5_references.clj")
      (get-in [:articles "references" :elements])
      first)
  => '{:type :code
       :mode :source
       :refer example.core/foo
       :origin :reference
       :indentation 0
       :code "(defn foo\n  [x]\n  (println x \"Hello, World!\"))"
       :tag "source-example-core--foo"})


(fact "t6 - test for link-stencil"
  (-> (doc/generate-article folio "stencil"
                            "test/documentation/short/t6_stencil.clj")
      (get-in [:articles "stencil" :elements]))
  => '[{:type :chapter, :title "Hello There", :tag "hello", :number "1"}
       {:type :section, :title "Hello World", :tag "world", :number "1.1"}
       {:type :paragraph, :text "1 1.1 0.1.0"}])


(fact "t7 - test for plugging more element processors on rendering side"
  (-> (doc/generate-article folio "stencil"
                            "test/documentation/short/t7_pluggable.clj")
      (get-in [:articles "stencil" :elements])
      first)
  => {:type :random, :title "Hello There", :tag "hello"})


(fact "t8 - test for generating citations"
  (-> (doc/generate-article folio "citation"
                            "test/documentation/short/t8_citation.clj")
      (get-in [:articles "citation" :elements])
      first)
  => {:type :paragraph, :text "This is a case of [[2](#2004-bower-thompson)]"})
