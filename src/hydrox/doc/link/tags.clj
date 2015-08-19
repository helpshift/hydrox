(ns hydrox.doc.link.tags
  (:require [hara.string.case :as case]))

(def tag-required?
  #{:chapter :section :subsection :subsubsection :appendix :citation})

(def tag-optional?
  #{:ns :reference :image :equation})

(defn inc-candidate
  "creates an incremental version of a name
   
   (inc-candidate \"hello\") => \"hello-0\"
   (inc-candidate \"hello-1\") => \"hello-2\""
  {:added "0.1"}
  [candidate]
  (if-let [[_ counter] (re-find #"-(\d+)$" candidate)]
    (let [len (count counter)
          num (Long/parseLong counter)]
      (str (subs candidate 0 (- (count candidate) len)) (inc num)))
    (str candidate "-0")))

(defn tag-string
  "creates a string that can be used as an anchor
   
   (tag-string \"hello.world/again\")
   => \"hello-world--again\""
  {:added "0.1"}
  [s]
  (-> (case/spear-case s)
      (.replaceAll "\\." "-")
      (.replaceAll "/" "--")
      (.replaceAll "[^\\d^\\w^-]" "")))

(defn create-candidate
  "creates a candidate tag from a variety of sources
 
   (create-candidate {:origin :ns :ns 'clojure.core})
   => \"ns-clojure-core\"
 
   (create-candidate {:title \"hello again\"})
   => \"hello-again\"
 
   (create-candidate {:type :image :src \"http://github.com/hello/gather.jpeg\"})
   => \"img-http----github-com--hello--gather-jpeg\""
  {:added "0.1"}
  [{:keys [origin title type] :as element}]
  (cond origin
        (case origin
          :ns (tag-string (str "ns-" (:ns element)))
          :reference (tag-string (str (name (:mode element)) "-" (:refer element))))

        title
        (tag-string title)

        (= :image type)
        (tag-string (str "img-" (:src element)))))

(defn create-tag
  "creates a tag from an element
 
   (let [tags (atom #{})
         result (create-tag {:title \"hello\"} tags)]
     [@tags result])
   => [#{\"hello\"} {:title \"hello\", :tag \"hello\"}]
 
   (let [tags (atom #{\"hello\"})
         result (create-tag {:title \"hello\"} tags)]
     [@tags result])
   => [#{\"hello\" \"hello-0\"} {:title \"hello\", :tag \"hello-0\"}]"
  {:added "0.1"}
  ([element tags]
   (create-tag element tags (create-candidate element)))
  ([element tags candidate]
   (cond (nil? candidate)
         element

         (get @tags candidate)
         (create-tag element tags (inc-candidate candidate))

         :else
         (do (swap! tags conj candidate)
             (assoc element :tag candidate)))))

(defn link-tags
  "creates a tag for elements within the article
   (-> {:articles {\"example\" {:elements [{:type :chapter :title \"hello world\"}
                                         {:type :chapter :title \"hello world\"}]}}}
       (collect/collect-tags \"example\")
       (link-tags \"example\"))
   => {:articles
       {\"example\"
        {:elements [{:type :chapter, :title \"hello world\", :tag \"hello-world\"}
                   {:type :chapter, :title \"hello world\", :tag \"hello-world-0\"}],
         :tags #{}}}}"
  {:added "0.1"}
  [{:keys [articles] :as folio} name]
  (let [tags (atom (get-in articles [name :tags]))]
    (let [auto-tag (->> (list (get-in articles [name :link :auto-tag])
                              (get-in folio [:meta :link :auto-tag])
                              true)
                        (drop-while nil?)
                        (first))
          auto-tag (cond (set? auto-tag) auto-tag
                         (false? auto-tag) #{}
                         (true? auto-tag) tag-optional?)]
      (->> (get-in articles [name :elements])
           (mapv (fn [element]
                     (cond (and (or (tag-required? (:type element))
                                    (auto-tag      (:type element))
                                    (auto-tag      (:origin element)))
                                (nil? (:tag element))
                                (not  (:hidden element)))
                           (create-tag element tags)

                           :else element)))
           (assoc-in folio [:articles name :elements])))))
