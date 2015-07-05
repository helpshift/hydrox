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
  ([v f]
   (seperate (rest v) f [] (if (first v)
                             [(first v)]
                             [])))
  ([[ele & more] f all current]
   (cond (nil? ele)
         (conj all current)

         (f ele)
         (recur more f (conj all current) [ele])

         :else
         (recur more f all (conj current ele)))))

(defn eq? [tag ele]
  (if (set? tag)
    (get tag (:type ele))
    (= tag (:type ele))))

(defn containify
  ([v]
   (->> (containify v [#{:appendix :chapter :generic} :section :subsection :subsubsection])
        (cons {:type :article})
        vec))
  ([v [tag & more]]
   (let [subv (seperate v (fn [ele] (eq? tag ele)))]
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
        {:type (:type head)
         :meta (dissoc head :type)
         :elements (vec (flatten (map mapify-unit more)))}

        :else
        (mapv mapify-unit (cons head more))))


(defn structure [v]
  (-> v
      (containify)
      (mapify)))





(comment
  (structure [{:type :generic}
                {:type :paragraph}
                {:type :paragraph}
                {:type :paragraph}
                {:type :chapter}
                {:type :paragraph}
                {:type :paragraph}
                {:type :section}
                {:type :paragraph}
                {:type :section}
                {:type :subsection}
                {:type :section}
                {:type :chapter}
                {:type :section}
                {:type :appendix}])
  
  
  (mapify
   (containify [{:type :generic}
                {:type :paragraph}
                {:type :paragraph}
                {:type :paragraph}
                {:type :chapter}
                {:type :paragraph}
                {:type :paragraph}
                {:type :section}
                {:type :paragraph}
                {:type :section}
                {:type :subsection}
                {:type :section}
                {:type :chapter}
                {:type :section}
                {:type :appendix}]))
  {:type :article, :head {},
   :elements [{:type :generic, :head {},
               :elements [{:type :paragraph} {:type :paragraph} {:type :paragraph}]}
              {:type :chapter, :head {},
               :elements [{:type :paragraph} {:type :paragraph}
                          {:type :section, :head {},
                           :elements [{:type :paragraph}]}
                          {:type :section, :head {},
                           :elements [{:type :subsection, :head {}, :elements []}]}
                          {:type :section, :head {}, :elements []}]}
              {:type :chapter, :head {},
               :elements [{:type :section, :head {}, :elements []}]}
              {:type :appendix, :head {}, :elements []}]}
  
  

  
  
  

  
  
  
  
  
  








  (comment

    (fact
      (inclusive :article :section) => true
      (inclusive :chapter :section) => true
      (inclusive :chapter :subsection) => true
      (inclusive :subsection :subsubsection) => true
      (inclusive :chapter :chapter) => false
      (inclusive :section :chapter) => false)))
