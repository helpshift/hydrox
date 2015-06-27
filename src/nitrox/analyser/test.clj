(ns nitrox.analyser.test
    (:require [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.walk :as walk]
            [hara.data.nested :as nested]
            [nitrox.analyser.common :as common]
            [nitrox.analyser.test
             [common :as test] clojure midje]))

(defn find-frameworks [ns-form]
  (let [store (atom #{})]
    (walk/postwalk (fn [form]
                     (if-let [k (test/frameworks form)]
                       (swap! store conj k)))
                   ns-form)
    @store))

(defmethod common/analyse-file :test [_ file]
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

