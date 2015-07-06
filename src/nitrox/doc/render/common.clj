(ns nitrox.doc.render.common
  (:require [nitrox.doc.render.util :as util]))

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
  (vec (concat [:div {:class :chapter}
                (if tag [:a {:name tag}])]
               (map #(render % folio) elements))))

(defmethod render
  :chapter
  [{:keys [tag text number title elements] :as element} folio]
  (println (dissoc element :elements))
  (vec (concat [:div {:class :chapter}
                (if tag [:a {:name tag}])
                [:h2 {:class :chapter} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :appendix
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :chapter}
                (if tag [:a {:name tag}])
                [:h2 {:class :appendix} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :section
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :section}
                (if tag [:a {:name tag}])
                [:h3 {:class :section} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :subsection
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :subsection}
                (if tag [:a {:name tag}])
                [:h3 {:class :subsection} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :subsubsection
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :subsubsection}
                (if tag [:a {:name tag}])
                [:h4 {:class :subsubsection} (str number "  &nbsp;&nbsp; " title)]]
               (map #(render % folio) elements))))

(defmethod render
  :code
  [{:keys [tag text code indentation] :as element} folio]
  [:div {:class :code}
   (if tag [:a {:name tag}])
   [:pre [:code
          (-> code
              (util/join)
              (util/basic-html-escape)
              (util/adjust-indent indentation))]]])

(defmethod render
  :block
  [element folio]
  (render (assoc element :type :code) folio))
