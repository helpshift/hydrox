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
  "finds the corresponding test framework

   (find-frameworks '(ns ...
                       (:use midje.sweet)))
   => #{:midje}"
  {:added "0.1"}
  [ns-form]
  (let [folio (atom #{})]
    (walk/postwalk (fn [form]
                     (if-let [k (test/frameworks form)]
                       (swap! folio conj k)))
                   ns-form)
    @folio))

(defmethod common/analyse-file
  :test
  [_ file opts]
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

(comment
  (println (get-in (analyse-test-file "test/example.clj")
                   '[clojure.core + :docs]))

  (analyse-test-file "test/fact.clj"))
