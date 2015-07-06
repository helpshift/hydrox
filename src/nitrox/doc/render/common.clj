(ns nitrox.doc.render.common
  (:require [nitrox.doc.render.util :as util]))

(defmulti render (fn [element folio] (:type element)))

(defmethod render
  :paragraph
  [{:keys [text indentation] :as element} folio]
  [:p {:class :paragraph}
   (-> text
       (util/markup)
       (util/basic-html-unescape)
       (util/adjust-indent indentation))])

(defmethod render
  :generic
  [{:keys [tag text elements]} folio]
  (vec (concat [:div {:class :chapter}
                (if tag [:a {:name tag}])]
               (map render elements))))

(defmethod render
  :chapter
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :chapter}
                (if tag [:a {:name tag}])
                [:h2 {:class :chapter} (str number "  &nbsp;&nbsp; " title)]]
               (map render elements))))

(defmethod render
  :appendix
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :chapter}
                (if tag [:a {:name tag}])
                [:h2 {:class :appendix} (str number "  &nbsp;&nbsp; " title)]]
               (map render elements))))

(defmethod render
  :section
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :section}
                (if tag [:a {:name tag}])
                [:h3 {:class :section} (str number "  &nbsp;&nbsp; " title)]]
               (map render elements))))

(defmethod render
  :subsection
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :subsection}
                (if tag [:a {:name tag}])
                [:h3 {:class :subsection} (str number "  &nbsp;&nbsp; " title)]]
               (map render elements))))

(defmethod render
  :subsubsection
  [{:keys [tag text number title elements]} folio]
  (vec (concat [:div {:class :subsubsection}
                (if tag [:a {:name tag}])
                [:h4 {:class :subsubsection} (str number "  &nbsp;&nbsp; " title)]]
               (map render elements))))

(defmethod render
  :code
  [{:keys [tag text indentation]} folio]
  [:div {:class :code}
   (if tag [:a {:name tag}])
   [:pre [:code
          (-> text
              (util/markup)
              (util/basic-html-escape)
              (util/adjust-indent indentation))]]])

(defmethod render
  :block
  [element folio]
  (render (assoc element :type :code) folio))
