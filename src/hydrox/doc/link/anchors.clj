(ns hydrox.doc.link.anchors)

(defn link-anchors-lu
  "creates the anchor lookup for tags and numbers
   (-> {:articles {\"example\" {:elements [{:type :chapter :tag \"hello\" :number \"1\"}]}}}
       (link-anchors-lu \"example\")
       :anchors-lu)
   => {\"example\" {:by-number {:chapter {\"1\" {:type :chapter, :tag \"hello\", :number \"1\"}}},
                  :by-tag {\"hello\" {:type :chapter, :tag \"hello\", :number \"1\"}}}}"
  {:added "0.1"}
  [{:keys [articles] :as folio} name]
  (let [anchors (->> (get-in articles [name :elements])
                     (filter :tag)
                     (map #(select-keys % [:type :tag :number])))]

    (->> anchors
         (reduce (fn [m {:keys [type tag number] :as anchor}]
                   (let [m (if number
                             (assoc-in m [:by-number type number] anchor)
                             m)]
                     (assoc-in m [:by-tag tag] anchor)))
                 {})
         (assoc-in folio [:anchors-lu name]))))

(defn link-anchors
  "creates a global anchors list based on the lookup
 
   (-> {:articles {\"example\" {:elements [{:type :chapter :tag \"hello\" :number \"1\"}]}}}
       (link-anchors-lu \"example\")
       (link-anchors \"example\")
       :anchors)
   => {\"example\" {\"hello\" {:type :chapter, :tag \"hello\", :number \"1\"}}}"
  {:added "0.1"}
  [{:keys [anchors-lu articles] :as folio} name]
  (->> (get anchors-lu name)
       :by-tag
       (assoc-in folio [:anchors name])))
