(ns hydrox.doc.render.article
  (:require [hydrox.doc.render.util :as util]
            [hydrox.doc.structure :as structure]))

(defmulti render (fn [element folio] (:type element)))

(defmethod render
  :paragraph
  [{:keys [text indentation] :as element} folio]
  [:div {:class :paragraph}
   (-> text
       (util/markup)
       (util/basic-html-unescape)
       (util/adjust-indent (or indentation 0)))])

(defmethod render
  :generic
  [{:keys [tag text elements]} folio]
  (vec (concat [:section]
               (map #(render % folio) elements))))

(defmethod render
  :chapter
  [{:keys [tag text number title elements] :as element} folio]
  (vec (concat [:section {:id tag :class :chapter}
                [:h2 {:class :chapter} (str number "  &nbsp;&nbsp; " title)]]
               (->> (structure/seperate #(= (:type %) :section) elements)
                    (map (fn [group]
                           (if (= :section (:type (first group)))
                             (render (first group) folio)
                             (vec (concat [:div {:class :group}]
                                          (map #(render % folio) group))))))))))

(defmethod render
  :appendix
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:section {:id tag :class :chapter}
                [:h2 {:class :chapter} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :section
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:section {:id tag :class :section}
                [:h3 {:class :section} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :subsection
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:section {:id tag :class :subsection}
                [:h4 {:class :subsection} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :subsubsection
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:section {:id tag :class :subsubsection}
                [:h4 {:class :subsubsection} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :code
  [{:keys [tag text code indentation] :as element} folio]
  [:div {:class :code :hljs :hljs :no-escape :no-escape :language :clojure}
   (-> code
       (util/join)
       (util/basic-html-escape)
       (util/adjust-indent indentation))])

(defmethod render
  :block
  [element folio]
  (render (assoc element :type :code) folio))

(defmethod render
  :image
  [{:keys [tag title text number] :as element} folio]
  [:div {:class "figure"}
     (if tag [:a {:name tag}])
     (if number
       [:h4 [:i (str "fig." number
                     (if title (str "  &nbsp;-&nbsp; " title)))]])
   [:img (dissoc element :number :type :tag :text :title)]])

(comment (defmethod render
           :article
           [element folio]
           (vec (concat [:div {:class :article}]
                        (map (fn [ele] (render ele folio))
                             (:elements element))))))
