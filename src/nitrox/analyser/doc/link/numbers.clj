(ns nitrox.analyser.doc.link.numbers)

(def new-counter
  {:chapter 0
   :section 0
   :subsection 0
   :subsubsection 0
   :code 0
   :image 0
   :equation 0})

(defn increment [count]
  (if (number? count)
    "A"
    (->> count
         first
         char
         int inc char str)))

(defn link-numbers-loop
  ([elements auto-number]
   (link-numbers-loop elements auto-number new-counter []))
  ([[{:keys [type origin] :as ele} & more :as elements]
    auto-number
    {:keys [chapter section subsection subsubsection code image equation] :as counter}
    output]
   (if (empty? elements)
     output
     (let [[numstring counter]
           (case type
             :chapter
             [(str (inc chapter))
              (assoc counter
                     :chapter (if (number? chapter)
                                (inc chapter)
                                0)
                     :section 0 :subsection 0 :subsubsection 0 :code 0)]
             :section
             [(str chapter "." (inc section))
              (assoc counter :section (inc section) :subsection 0 :subsubsection 0)]

             :subsection
             [(str chapter "." section "." (inc subsection))
              (assoc counter :subsection (inc subsection) :subsubsection 0)]

             :subsubsection
             [(str chapter "." section "." subsection "." (inc subsubsection))
              (assoc counter :subsubsection (inc subsubsection))]

             :appendix
             [(str (increment chapter))
              (assoc counter
                     :chapter (increment chapter)
                     :section 0 :subsection 0 :subsubsection 0 :code 0)]

             (if (and (#{:code :image :equation} type)
                      (or (auto-number type)
                          (auto-number origin)
                          (:numbered ele))
                      (not (or (:hidden ele)
                               (false? (:numbered ele)))))
               (case type
                 :code
                 [(str chapter "." (inc code))
                      (assoc counter :code (inc code))]
                 :image
                 [(str (inc image))
                  (assoc counter :image (inc image))]

                 :equation
                 [(str (inc equation))
                  (assoc counter :equation (inc equation))])

               [nil counter]))
           ele (if numstring
                 (assoc ele :number numstring)
                 ele)]
       (recur more auto-number counter (conj output ele))))))

(defn link-numbers [{:keys [articles] :as folio} name]
  (let [auto-number (->> (list (get-in articles [name :link :auto-number])
                               (get-in folio [:meta :link :auto-number])
                               true)
                         (drop-while nil?)
                         (first))
        auto-number  (cond (set? auto-number) auto-number
                           (false? auto-number) #{}
                           (true? auto-number) #{:image :equation})]
    (update-in folio [:articles name :elements]
               link-numbers-loop auto-number)))
