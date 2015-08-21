(ns hydrox.doc.parse-test
  (:use midje.sweet)
  (:require [hydrox.doc.parse :refer :all]
            [rewrite-clj.zip :as z]))

^{:refer hydrox.doc.parse/parse-ns-form :added "0.1"}
(fact "converts a ns zipper into an element"

  (-> (z/of-string "(ns example.core)")
      (parse-ns-form))
  => '{:type :ns-form
       :indentation 0
       :ns example.core
       :code "(ns example.core)"})

^{:refer hydrox.doc.parse/code-form :added "0.1"}
(fact "converts a form zipper into a code string"
  
  (-> (z/of-string "(fact (+ 1 1) \n => 2)")
      (code-form 'fact))
  => "(+ 1 1) \n => 2")

^{:refer hydrox.doc.parse/parse-fact-form :added "0.1"}
(fact "convert a fact zipper into an element"

  (-> (z/of-string "(fact (+ 1 1) \n => 2)")
      (parse-fact-form))
  => {:type :block :indentation 2 :code "(+ 1 1) \n => 2"})

^{:refer hydrox.doc.parse/parse-comment-form :added "0.1"}
(fact "convert a comment zipper into an element"

  (-> (z/of-string "(comment (+ 1 1) \n => 2)")
      (parse-comment-form))
  => {:type :block :indentation 2 :code "(+ 1 1) \n => 2"})

^{:refer hydrox.doc.parse/parse-paragraph :added "0.1"}
(fact "converts a string zipper into an element"
  (-> (z/of-string "\"this is a paragraph\"")
      (parse-paragraph))
  => {:type :paragraph :text "this is a paragraph"})

^{:refer hydrox.doc.parse/parse-directive :added "0.1"}
(fact "converts a directive zipper into an element"
  (-> (z/of-string "[[:chapter {:title \"hello world\"}]]")
      (parse-directive))
  => {:type :chapter :title "hello world"}

  (binding [*namespace* 'example.core]
    (-> (z/of-string "[[:ns {:title \"hello world\"}]]")
        (parse-directive)))
  => {:type :ns, :title "hello world", :ns 'example.core})

^{:refer hydrox.doc.parse/parse-attribute :added "0.1"}
(fact "coverts an attribute zipper into an element"
  (-> (z/of-string "[[{:title \"hello world\"}]]")
      (parse-attribute))
  => {:type :attribute, :title "hello world"})

^{:refer hydrox.doc.parse/parse-code-directive :added "0.1"}
(fact "coverts an code directive zipper into an element"
  (-> (z/of-string "[[:code {:language :ruby} \"1 + 1 == 2\"]]")
      (parse-code-directive))
  => {:type :block, :indentation 0 :code "1 + 1 == 2" :language :ruby})

^{:refer hydrox.doc.parse/parse-whitespace :added "0.1"}
(fact "coverts a whitespace zipper into an element"
  (-> (z/of-string "1 2 3")
      (z/right*)
      (parse-whitespace))
  => {:type :whitespace, :code [" "]})

^{:refer hydrox.doc.parse/parse-code :added "0.1"}
(fact "coverts a code zipper into an element"
  (-> (z/of-string "(+ 1 1) (+ 2 2)")
      (parse-code))
  => {:type :code, :indentation 0, :code ["(+ 1 1)"]})

^{:refer hydrox.doc.parse/parse-loop :added "0.1"}
(fact "the main loop for the parser"
  (-> (z/of-string "(ns example.core) 
                    [[:chapter {:title \"hello\"}]] 
                    (+ 1 1) 
                    (+ 2 2)")
      (parse-loop {}))
  => [{:type :ns-form, :indentation 0, :ns 'example.core, :code "(ns example.core)"}
      {:type :chapter, :title "hello"}
      {:type :code, :indentation 0, :code ["(+ 1 1)"
                                           " "
                                           "\n"
                                           "                    "
                                           "(+ 2 2)"]}])
