(ns hydrox.analyse.test
    (:require [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.walk :as walk]
            [hara.data.nested :as nested]
            [hydrox.analyse.common :as common]
            [hydrox.analyse.test
             [common :as test] clojure midje]))

(defn find-frameworks
  "find test frameworks given a namespace form
   (find-frameworks '(ns ...
                       (:use midje.sweet)))
   => #{:midje}
 
   (find-frameworks '(ns ...
                       (:use clojure.test)))
   => #{:clojure}"
  {:added "0.1"}
  [ns-form]
  (let [folio (atom #{})]
    (walk/postwalk (fn [form]
                     (if-let [k (test/frameworks form)]
                       (swap! folio conj k)))
                   ns-form)
    @folio))

(defn analyse-test-file
  [file opts]
  (let [zloc   (source/of-file file)
        nsloc  (query/$ zloc [(ns | _ & _)] {:walk :top
                                             :return :zipper
                                             :first true})
        nsp        (source/sexpr nsloc)
        ns-form    (-> nsloc source/up source/sexpr)
        frameworks (find-frameworks ns-form)]
    (->> frameworks
         (map (fn [framework]
                (test/analyse-test framework zloc)))
         (apply nested/merge-nested))))

(defmethod common/analyse-file
  :test
  [_ file opts]
  (analyse-test-file file opts))
