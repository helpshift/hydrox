(ns nitrox.analyser.doc.parse
  (:require [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]
            [nitrox.analyser.doc.checks :as checks]))

(def ^:dynamic *spacing* 2)
(def ^:dynamic *indentation* 0)
(def ^:dynamic *namespace* nil)

(declare parse-file parse-facts-form)

(defn parse-ns-form [zloc]
  {:type :ns-form
   :indentation *indentation*
   :ns   (-> zloc source/sexpr second)
   :code (source/string zloc)})

(defn code-form [zloc symbol]
  (let [s (source/string zloc)]
    (-> (.substring s 1 (dec (.length s)))
        (.replaceFirst (str symbol "(\\s+)?") ""))))

(defn parse-fact-form [zloc]
  {:type :block
   :indentation (+ *indentation* *spacing*)
   :code (code-form zloc "fact")})

(defn parse-facts-form [zloc]
  {:type :facts})

(defn parse-comment-form [zloc]
  {:type :block
   :indentation (+ *indentation* *spacing*)
   :code (code-form zloc "comment")})

(defn parse-paragraph [zloc]
  {:type :paragraph
   :text (source/sexpr zloc)})

(defn parse-directive [zloc]
  (let [tloc       (-> zloc source/down source/down)
        tag        (-> tloc source/sexpr)
        attributes (-> tloc source/right source/sexpr)
        directive  (merge {:type tag} attributes)]
    (if (= :ns tag)
      (assoc directive :ns *namespace*)
      directive)))

(defn parse-attribute [zloc]
  (let [attributes (-> zloc source/down source/down source/sexpr)]
    (assoc attributes :type :attribute)))

(defn parse-whitespace [zloc]
  {:type :whitespace
   :code [(node/string (source/node zloc))]})

(defn parse-code [zloc]
  {:type :code
   :indentation *indentation*
   :code [(node/string (source/node zloc))]})

(defn wrap-meta [f]
  (fn [zloc]
    (if (= :meta (source/tag zloc))
      (f (-> zloc source/down source/right))
      (f zloc))))

(defn parse-single [zloc]
  (cond (checks/whitespace? zloc)
        (parse-whitespace zloc)

        (checks/ns? zloc)
        (parse-ns-form zloc)

        (checks/fact? zloc)
        ((wrap-meta parse-fact-form) zloc)

        (checks/facts? zloc)
        ((wrap-meta parse-facts-form) zloc)

        (checks/comment? zloc)
        ((wrap-meta parse-comment-form) zloc)

        (checks/directive? zloc)
        ((wrap-meta parse-directive) zloc)

        (checks/attribute? zloc)
        ((wrap-meta parse-attribute) zloc)

        (checks/paragraph? zloc)
        (parse-paragraph zloc)

        :else (parse-code zloc)))

(defn append-code [current new]
  (update-in current [:code] #(apply conj % (:code new))))

(defn merge-current [output current]
  (cond (nil? current) output

        (= :whitespace (:type current)) output

        :else (conj output current)))

(defn parse-loop
  ([zloc opts] (parse-loop zloc opts nil []))
  ([zloc opts current output]
   ;;(println zloc (source/node zloc))
   (cond (nil? zloc)
         (merge-current output current)

         :else
         (let [element (parse-single zloc)]
           ;;(prn "ELEMENT" element)
           (cond (= (:type current) :attribute)
                 (if (not= :whitespace (:type element))
                   (recur (source/right* zloc) opts (merge current element) output)
                   (recur (source/right* zloc) opts current output))

                 (nil? (:type element))
                 (throw (Exception. (str "element should have a type " element (:type element))))

                 (and (#{:code :whitespace} (:type element))
                      (= (:type current) :code))
                 (recur (source/right* zloc) opts (append-code current element) output)

                 :else
                 (case (:type element)
                   :ns-form    (binding [*namespace* (:ns element)]
                                 (parse-loop (source/right* zloc) opts element (merge-current output current)))
                   :attribute  (recur (source/right* zloc) opts element (merge-current output current))
                   :file       (recur (source/right* zloc) opts nil (apply conj output (parse-file (:src element) opts)))
                   :facts      (recur (source/right* zloc) opts nil
                                      (let [sub (binding [*indentation* (+ *indentation* *spacing*)]
                                                  (parse-loop (-> zloc source/down source/right) opts))]
                                        (apply conj (merge-current output current) sub)))
                   (recur (source/right* zloc) opts element (merge-current output current))))))))

(defn parse-file [file opts]

  ;; For developement purposes
  (parse-loop (source/of-file (str (:root opts) "/" file)) opts))


(comment
  (parse-file "test/documentation/example_test.clj" {:root "/Users/chris/Development/chit/nitrox"}))
