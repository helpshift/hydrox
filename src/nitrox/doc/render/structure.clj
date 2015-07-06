(ns nitrox.doc.render.structure)

(def containers
  #{:article :chapter :appendix :section :subsection :subsubsection :generic})

(def hierarchy
  (-> (make-hierarchy)
      (derive :article :generic)
      (derive :article :chapter)
      (derive :article :appendix)
      (derive :container :section)
      (derive :appendix :section)
      (derive :chapter :section)
      (derive :section :subsection)
      (derive :subsection :subsubsection)))

(defn inclusive [x y]
  (if (get containers y)
    (if (not (isa? hierarchy y x))
      true
      false)
    true))

(defn seperate
  ([f v]
   (seperate f (rest v) [] (if (first v)
                             [(first v)]
                             [])))
  ([f [ele & more] all current]
   (cond (nil? ele)
         (conj all current)

         (f ele)
         (recur f more (conj all current) [ele])

         :else
         (recur f more all (conj current ele)))))

(defn containify
  ([v]
   (->> (containify v [#{:appendix :chapter :generic} :section :subsection :subsubsection])
        (cons {:type :article})
        vec))
  ([v [tag & more]]
   (let [eq? (fn [tag ele]
               (if (set? tag)
                 (get tag (:type ele))
                 (= tag (:type ele))))
         subv (seperate (fn [ele] (eq? tag ele)) v)]
     (cond (empty? more)
           subv
           
           :else
           (mapv (fn [[head & body]]
                   (cond 
                     (empty? body) [head]

                     :else
                     (vec (cons head (containify body more)))))
                subv)))))

(declare mapify)

(defn mapify-unit [x]
  (cond (map? x) x
        :else (mapify x)))

(defn mapify [[head & more]]
  (cond (get containers (:type head))
        (assoc head :elements (vec (flatten (map mapify-unit more))))

        :else
        (mapv mapify-unit (cons head more))))

(defn structure [v]
  (-> v
      (containify)
      (mapify)))
