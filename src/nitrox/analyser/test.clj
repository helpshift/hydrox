(ns nitrox.analyser.test
    (:require [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [clojure.walk :as walk]
            [hara.data.nested :as nested]
            [nitrox.analyser.test.common :as common]
            [nitrox.analyser.test.clojure]
            [nitrox.analyser.test.midje]))

(defn find-frameworks [ns-form]
  (let [store (atom #{})]
    (walk/postwalk (fn [form]
                     (if-let [k (common/frameworks form)]
                       (swap! store conj k)))
                   ns-form)
    @store))

(defn analyse-test-file [file]
  (let [zloc   (source/of-file file)
        nsloc  (query/$ zloc [(ns | _ & _)] {:walk :top
                                             :return :zipper
                                             :first true})
        nsp        (source/sexpr nsloc)
        ns-form    (-> nsloc source/up source/sexpr)
        frameworks (find-frameworks ns-form)]
    (->> frameworks
         (map (fn [framework]
                (common/analyse-test framework zloc)))
         (apply nested/merge-nested))))

(comment
  (println (get-in (analyse-test-file "test/example.clj")
                   '[clojure.core + :docs]))
  
  (analyse-test-file "test/fact.clj"))

