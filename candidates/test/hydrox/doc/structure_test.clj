(ns hydrox.doc.structure-test
  (:use midje.sweet)
  (:require [hydrox.doc.structure :refer :all]))

^{:refer hydrox.doc.structure/inclusive :added "0.1"}
(fact "determines which sections are contained by the other"
  (inclusive :article :section) => true

  (inclusive :chapter :subsection) => true

  (inclusive :chapter :chapter) => false

  (inclusive :section :chapter) => false)

 ^{:refer hydrox.doc.structure/seperate :added "0.1"}
(fact "groups elements in an array "
  (seperate #(= 1 %) [1 2 2 1 3 4 5])
  => [[1 2 2] [1 3 4 5]])

^{:refer hydrox.doc.structure/containify :added "0.1"}
(fact "makes a nested vector object from a sequence of elements"
  (containify [{:type :generic}
               {:type :paragraph}
               {:type :chapter}
               {:type :paragraph}
               {:type :section}
               {:type :paragraph}
               {:type :subsection}
               {:type :paragraph}
               {:type :section}
               {:type :chapter}
               {:type :section}
               {:type :appendix}])
  => [{:type :article}
      [{:type :generic}
       [{:type :paragraph}]]
      [{:type :chapter}
       [{:type :paragraph}]
       [{:type :section}
        [{:type :paragraph}]
        [{:type :subsection}
         [{:type :paragraph}]]]
       [{:type :section}]]
      [{:type :chapter}
       [{:type :section}]]
      [{:type :appendix}]])

^{:refer hydrox.doc.structure/containify :added "0.1"}
(fact "makes a nested vector object from a sequence of elements"

  (containify [{:type :generic}
               {:type :paragraph}
               {:type :chapter}
               {:type :paragraph}
               {:type :section}
               {:type :paragraph}
               {:type :subsection}
               {:type :paragraph}
               {:type :section}
               {:type :chapter}
               {:type :section}
               {:type :appendix}]))

^{:refer hydrox.doc.structure/structure :added "0.1"}
(fact "creates a nested map structure of elements and their containers"
  (structure [{:type :generic}
              {:type :paragraph}
               {:type :chapter}
               {:type :paragraph}
               {:type :section}
               {:type :paragraph}
               {:type :subsection}
               {:type :paragraph}
               {:type :section}
               {:type :chapter}
               {:type :section}
               {:type :appendix}])
  => {:type :article,
      :meta {},
      :elements [{:type :generic,
                  :meta {},
                  :elements [{:type :paragraph}]}
                 {:type :chapter,
                  :meta {},
                  :elements [{:type :paragraph}
                             {:type :section,
                              :meta {},
                              :elements [{:type :paragraph}
                                         {:type :subsection,
                                          :meta {},
                                          :elements [{:type :paragraph}]}]}
                             {:type :section,
                              :meta {},
                              :elements []}]}
                 {:type :chapter,
                  :meta {},
                  :elements [{:type :section,
                              :meta {},
                              :elements []}]}
                 {:type :appendix,
                  :meta {},
                  :elements []}]})
