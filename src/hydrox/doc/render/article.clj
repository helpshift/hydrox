(ns hydrox.doc.render.article
  (:require [hydrox.doc.render.util :as util]
            [hydrox.doc.structure :as structure]
            [hydrox.doc.link.references :as references]
            [rewrite-clj.node :as node]
            [clojure.string :as string]))

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
  [{:keys [tag text code indentation lang number title] :as element} folio]
  [:div {:class :code}
   (if tag [:a {:name tag}])
   (if number
     [:h5 (str "e." number
               (if title (str "  &nbsp;-&nbsp; " title)))])
   [:div {:hljs :hljs :no-escape :no-escape :language (or lang :clojure)}
    (-> code
        (util/join)
        (util/basic-html-escape)
        (util/adjust-indent indentation)
        (string/trimr)
        (string/trim-newline))]])

(defmethod render
  :block
  [element folio]
  (render (assoc element :type :code) folio))

(defmethod render
  :image
  [{:keys [tag title text number] :as element} folio]
  [:div {:class :figure}
   (if tag [:a {:name tag}])
   [:div {:class "img"}
    [:img (dissoc element :number :type :tag :text :title)]]
   (if number
     [:h4 [:i (str "fig." number
                   (if title (str "  &nbsp;-&nbsp; " title)))]])])

(defmethod render
  :namespace
  [{:keys [mode] :as element} folio])

(defn render-api-index [namespace tag nsp]
  (->> nsp
       (map first)
       (map (fn [sym]
              [:a {:href (str "#" tag "--" sym)} (str sym)]))
       (#(interleave % (repeat "&nbsp;&nbsp;")))
       (apply vector :div [:a {:name tag}]
              [:h4 [:i "API"]])))

(defn render-api-elements [namespace tag nsp]
  (->> nsp
       (mapv (fn [[func data]]
               [:div
                [:a {:name (str tag "--" (name func))}]
                [:h4 (name func)
                 " " [:a {:href (str "#" tag)} "&#9652;"]]
                [:div {:hljs :hljs :no-escape :no-escape :language :clojure}
                 (with-redefs [hydrox.meta.util/escape-newlines identity]
                   (-> (:docs data)
                       (references/process-doc-nodes)
                       (util/join)
                       (util/basic-html-escape)
                       (util/adjust-indent 2)
                       (string/trimr)
                       (string/trim-newline)))]]))
       (apply vector :div)))

(defmethod render
  :api
  [{:keys [namespace tag] :as element} folio]
  (let [tag (or tag (str "api-" (.replaceAll ^String namespace "\\." "-")))
        nsp (-> folio
                 :references
                 (get (symbol namespace))
                 (->> (filter (fn [[_ data]] (:docs data)))
                      (sort-by first)))]
    [:div {:class :api}
     [:hr]
     (render-api-index namespace tag nsp)
     [:hr]
     (render-api-elements namespace tag nsp)]))

(comment

  [:p (-> folio
           :references
           (get (symbol namespace))
           keys
           (map str))]

  #_(-> folio
          :references
          (get (symbol namespace))
          first
          second
          :docs
          (references/process-doc-nodes)))
