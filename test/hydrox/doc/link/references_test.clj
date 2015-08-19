(ns hydrox.doc.link.references-test
  (:use midje.sweet)
  (:require [hydrox.doc.link.references :refer :all]
            [rewrite-clj.zip :as z]))

^{:refer hydrox.doc.link.references/process-doc-nodes :added "0.1"}
(fact "treat test nodes specially when rendering code"

  (->> (z/of-string "(+ 1 1) => (+ 2 2)")
       (iterate z/right*)
       (take-while identity)
       (map z/node)
       (process-doc-nodes))
  => "(+ 1 1)\n=>\n(+ 2 2)")

^{:refer hydrox.doc.link.references/link-references :added "0.1"}
(fact "link code for elements to references"

  (link-references {:articles {"example" {:elements [{:type :reference :refer 'example.core/hello}]}}
                    :references '{example.core {hello {:docs []
                                                       :source "(defn hello [] 1)"}}}}
                   "example")
  => '{:articles {"example"
                  {:elements [{:type :code,
                               :refer example.core/hello,
                               :origin :reference,
                               :indentation 0,
                               :code "(defn hello [] 1)",
                               :mode :source}]}},
       :references {example.core {hello {:docs [], :source "(defn hello [] 1)"}}}})




