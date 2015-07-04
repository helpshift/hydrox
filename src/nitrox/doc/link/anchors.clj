(ns nitrox.doc.link.anchors)

(defn link-anchors-lu [{:keys [articles] :as folio} name]
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

(defn link-anchors [{:keys [anchors-lu articles] :as folio} name]
  (->> (get anchors-lu name)
       :by-tag
       (assoc-in folio [:anchors name])))
