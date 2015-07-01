(ns nitrox.analyser.doc.link.tags
  (:require [hara.string.case :as case]))

(defn collect-tags [articles]
  (reduce-kv (fn [m article elements]
               (assoc m article
                      (reduce (fn [m {:keys [tag] :as ele}]
                                (cond (nil? tag) m

                                      (get m tag) (do (println "There is already an existing tag for" ele)
                                                      m)
                                      :else (conj m tag)))
                              #{}
                              elements)))
             {}
             articles))

(def tag-required?
  #{:chapter :section :subsection :subsubsection :appendix})

(def tag-optional?
  #{:ns :reference :image :equation})

(defn inc-candidate [candidate]
  (if-let [[_ counter] (re-find #"-(\d+)$" candidate)]
    (let [len (count counter)
          num (Long/parseLong counter)]
      (str (subs candidate 0 (- (count candidate) len)) (inc num)))
    (str candidate "-0")))

(defn tag-string [s]
  (-> (case/spear-case s)
      (.replaceAll "\\." "-")
      (.replaceAll "/" "--")))

(defn create-candidate [{:keys [origin title type] :as ele}]
  (cond origin
        (case origin
          :ns (tag-string (str "ns-" (:ns ele)))
          :reference (tag-string (str (name (:mode ele)) "-" (:refer ele))))

        title
        (case/spear-case title)

        (= :image type)
        (tag-string "img-" (:src ele))))

(defn create-tag
  ([ele article tags]
   (create-tag ele article tags (create-candidate ele)))
  ([ele article tags candidate]
   (cond (nil? candidate)
         ele

         (get-in @tags [article candidate])
         (create-tag ele article tags (inc-candidate candidate))

         :else
         (do (swap! tags update-in [article] conj candidate)
             (assoc ele :tag candidate)))))

(defn link-tags [folio]
  (let [tags (atom (:tags folio))
        articles (reduce-kv (fn [m article elements]
                              (let [auto-tag (->> (list (get-in folio [:articles article :link :auto-tag])
                                                        (get-in folio [:meta :link :auto-tag])
                                                        true)
                                                  (drop-while nil?)
                                                  (first))
                                    auto-tag (cond (set? auto-tag) auto-tag
                                                   (false? auto-tag) #{}
                                                   (true? auto-tag) tag-optional?)]
                                (assoc m article
                                       (mapv (fn [ele]
                                               (cond (and (or (tag-required? (:type ele))
                                                              (auto-tag      (:type ele))
                                                              (auto-tag      (:origin ele)))
                                                          (-> ele :tag nil?)
                                                          (not (:hidden ele)))
                                                     (create-tag ele article tags)

                                                     :else ele))
                                             elements))))
                            {}
                            (:results folio))]
    (assoc folio :tags @tags :results articles)))

(defn generate-tags [folio]
  (let [tags  (collect-tags (:results folio))]
    (-> folio
        (assoc :tags tags)
        (link-tags))))
