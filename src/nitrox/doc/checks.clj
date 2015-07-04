(ns nitrox.doc.checks
  (:require [jai.query :as query]
            [rewrite-clj.zip :as source]
            [rewrite-clj.node :as node]))

(def directives
  #{:article :file :reference :ns
    :appendix :chapter
    :section :subsection :subsubsection
    :image :paragraph :code
    :equation :citation})

(defn wrap-meta [f]
  (fn [zloc selector]
    (if (= :meta (source/tag zloc))
      (f (-> zloc source/down source/right) selector)
      (f zloc selector))))

(defn directive?
  "checks if the location contains a directive

   (directive? (source/of-string \"[[:ns {}]]\"))
   => true

   (directive? (source/of-string \"[[:ns {}]]\") :ns)
   => true"
  {:added "0.2"}
  ([zloc]
   ((wrap-meta query/match) zloc {:pattern [[#'keyword? #'map?]]}))
  ([zloc kw]
   ((wrap-meta query/match) zloc {:pattern [[kw #'map?]]})))

(defn attribute?
  "checks if the location contains an attribute

   (attribute? (source/of-string \"[[{:count 1}]]\"))
   => true"
  {:added "0.2"}
  [zloc]
  ((wrap-meta query/match) zloc {:pattern [[#'map?]]}))

(defn ns?
  "checks if the location contains an ns form

   (ns? (source/of-string \"^:hello (ns clojure.core)\"))
   => true"
  {:added "0.2"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'ns}))

(defn fact?
  "checks if the location contains a fact form

   (fact? (source/of-string \"^:hello (fact \\\"hello there\\\")\"))
   => true"
  {:added "0.2"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'fact}))

(defn facts?
  "checks if the location contains a fact form

   (facts? (source/of-string \"^:hello (facts \\\"hello there\\\")\"))
   => true"
  {:added "0.2"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'facts}))

(defn comment?
  "checks if the location contains a comment form

   (comment? (source/of-string \"^:hello (comment hello there)\"))
   => true"
  {:added "0.2"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'comment}))

(defn paragraph?
  "checks if the location contains a paragraph (string)

   (paragraph? (source/of-string \"\\\"hello there\\\"\"))
   => true"
  {:added "0.2"}
  [zloc]
  (string? (source/sexpr zloc)))

(defn whitespace?
  "checks if the location contains whitespace

   (whitespace? (source/right* (source/of-string \"() ()\")))
   => true"
  {:added "0.2"}
  [zloc]
  (node/whitespace-or-comment? (source/node zloc)))
