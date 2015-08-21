(ns hydrox.doc.checks
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
  "checks if the element is a directive.
 
   (-> \"[[:chapter {:title \\\"A Story\\\"}]]\"
       z/of-string
       directive?)
   => true"
  {:added "0.1"}
  ([zloc]
   ((wrap-meta query/match) zloc {:pattern [[#'keyword? #'map?]]}))
  ([zloc kw]
   ((wrap-meta query/match) zloc {:pattern [[kw #'map?]]})))

(defn attribute?
  "checks if the element is an attribute.
 
   (-> \"[[{:title \\\"A Story\\\"}]]\"
       z/of-string
       attribute?)
   => true"
  {:added "0.1"}
  [zloc]
  ((wrap-meta query/match) zloc {:pattern [[#'map?]]}))

(defn code-directive?
  [zloc]
  ((wrap-meta query/match) zloc {:pattern [[:code #'map? #'string?]]}))

(defn ns?
  "checks if the element is a ns form
 
   (-> \"(ns ...)\"
       z/of-string
       ns?)
   => true"
  {:added "0.1"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'ns}))

(defn fact?
  "checks if the element is a fact form
 
   (-> \"(fact ...)\"
       z/of-string
       fact?)
   => true"
  {:added "0.1"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'fact}))

(defn facts?
  "checks if the element is a facts form
 
   (-> \"(facts ...)\"
       z/of-string
       facts?)
   => true"
  {:added "0.1"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'facts}))

(defn comment?
  "checks if the element is a comment form"
  {:added "0.1"}
  [zloc]
  ((wrap-meta query/match) zloc {:form 'comment}))

(defn paragraph?
  "checks if the element is a paragraph (string)"
  {:added "0.1"}
  [zloc]
  (string? (source/sexpr zloc)))

(defn whitespace?
  "checks if the element is a whitespace element"
  {:added "0.1"}
  [zloc]
  (node/whitespace-or-comment? (source/node zloc)))
