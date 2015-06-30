(ns nitrox.analyser.doc.collect
  (:require [hara.data.nested :as nested]))

(defn collect-namespaces [{:keys [articles] :as store} name]
  (->> (get-in articles [name :elements])
       (filter #(-> % :type (= :ns-form)))
       (map (juxt :ns identity))
       (into {})
       (update-in store [:namespaces] (fnil nested/merge-nested {}))))

(defn collect-article [{:keys [articles] :as store} name]
  (->> (get-in articles [name :elements])
       (filter #(-> % :type (= :article)))
       (map    #(update-in store [:articles name :meta] (fnil nested/merge-nested %)))))

(defn collect-global [{:keys [articles] :as store} name]
  (->> (get-in articles [name :elements])
       (filter #(-> % :type (= :global)))
       (map    #(update-in store [:meta] (fnil nested/merge-nested %)))))

(comment


  {:meta        {}
   :articles    {"ova"   {:meta     <>
                          :elements []}}
   :project     <>
   :registry    <>
   :references  <>
   :namespaces  <>}

  (eduction (filter odd?)
            [1 2 3 4 5])

  (transduce collect-xf
             conj
             {}
             (parse/parse-file "test/documentation/example_test.clj"
                               {:root "/Users/chris/Development/chit/nitrox"}))

  (./source transduce)
  (./source map)
  (./source into))


(comment
  (require '[nitrox.analyser.doc.parse :as parse])

  (into {} collect-xf (parse/parse-file "test/documentation/example_test.clj" {:root "/Users/chris/Development/chit/nitrox"}))


  (collect-namespaces (parse/parse-file "test/documentation/example_test.clj" {:root "/Users/chris/Development/chit/nitrox"})
                      {})

  (collect-namespaces (parse/parse-file "test/documentation/sub_test.clj" {:root "/Users/chris/Development/chit/nitrox"})))
