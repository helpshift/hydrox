(ns hydrox.doc.render.navigation
  (:require [hydrox.doc.render.util :as util]))

(defmulti render (fn [element folio] (:type element)))

(defmethod render
  :chapter
  [{:keys [tag number title elements] :as element} folio]
  [:li ;;{:class :chapter}
       [:a {:href (str "#" tag)} (str number "  &nbsp;&nbsp; " title)]
   (let [sections (filter (fn [e] (= :section (:type e))) elements)]
     (if-not (empty? sections)
       (vec (concat [:ul {:class :nav}]
                    (map #(render % folio) sections)))))])

(defmethod render
  :appendix
  [{:keys [tag number title elements]} folio]
  [:li ;;{:class :appendix}
   [:a {:href (str "#" tag)} (str number "  &nbsp;&nbsp; " title)]
   (let [sections (filter (fn [e] (= :section (:type e))) elements)]
     (if-not (empty? sections)
       (vec (concat [:ul {:class :nav}]
                    (map #(render % folio) sections)))))])

(defmethod render
  :section
  [{:keys [tag number title]} folio]
  [:li ;;{:class :section}
       [:a {:href (str "#" tag)} (str number "  &nbsp;&nbsp; " title)]])
