(ns nitrox.doc.collect
  (:require [hara.data.nested :as nested]))

(defn collect-namespaces [{:keys [articles] :as folio} name]
  (let [namespaces (->> (get-in articles [name :elements])
                        (filter #(-> % :type (= :ns-form)))
                        (map (juxt :ns identity))
                        (into {}))]
    (-> folio
        (update-in [:namespaces] (fnil nested/merge-nested {}) namespaces)
        (update-in [:articles name :elements]
                   (fn [elements] (filter #(-> % :type (not= :ns-form)) elements))))))

(defn collect-article [{:keys [articles] :as folio} name]
  (let [articles (->> (get-in articles [name :elements])
       (filter #(-> % :type (= :article)))
       (apply nested/merge-nested {}))]
    (-> folio
        (update-in [:articles name :meta] (fnil nested/merge-nested {}) articles)
        (update-in [:articles name :elements]
                   (fn [elements] (filter #(-> % :type (not= :article)) elements))))))

(defn collect-global [{:keys [articles] :as folio} name]
  (let [global (->> (get-in articles [name :elements])
                    (filter #(-> % :type (= :global)))
                    (apply nested/merge-nested {}))]
    (-> folio
        (update-in [:meta] (fnil nested/merge-nested {}) global)
        (update-in [:articles name :elements]
                   (fn [elements] (filter #(-> % :type (not= :global)) elements))))))

(defn collect-tags [{:keys [articles] :as folio} name]
  (->> (get-in articles [name :elements])
       (reduce (fn [m {:keys [tag] :as ele}]
                                (cond (nil? tag) m

                                      (get m tag) (do (println "There is already an existing tag for" ele)
                                                      m)
                                      :else (conj m tag)))
               #{})
       (assoc-in folio [:articles name :tags])))

(defn collect-citations [{:keys [articles] :as folio} name]
  (let [citations (->> (get-in articles [name :elements])
                       (filter #(-> % :type (= :citation))))]
    (-> folio
        (assoc-in  [:articles name :citations] citations)
        (update-in [:articles name :elements]
                   (fn [elements] (filter #(-> % :type (not= :citation)) elements))))))

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
  (require '[nitrox.doc.parse :as parse])

  (into {} collect-xf (parse/parse-file "test/documentation/example_test.clj" {:root "/Users/chris/Development/chit/nitrox"}))


  (collect-namespaces (parse/parse-file "test/documentation/example_test.clj" {:root "/Users/chris/Development/chit/nitrox"})
                      {})

  (collect-namespaces (parse/parse-file "test/documentation/sub_test.clj" {:root "/Users/chris/Development/chit/nitrox"})))
