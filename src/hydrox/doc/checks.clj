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
  ([zloc]
   ((wrap-meta query/match) zloc {:pattern [[#'keyword? #'map?]]}))
  ([zloc kw]
   ((wrap-meta query/match) zloc {:pattern [[kw #'map?]]})))

(defn attribute?
  [zloc]
  ((wrap-meta query/match) zloc {:pattern [[#'map?]]}))

(defn ns?
  [zloc]
  ((wrap-meta query/match) zloc {:form 'ns}))

(defn fact?
  [zloc]
  ((wrap-meta query/match) zloc {:form 'fact}))

(defn facts?
  [zloc]
  ((wrap-meta query/match) zloc {:form 'facts}))

(defn comment?
  [zloc]
  ((wrap-meta query/match) zloc {:form 'comment}))

(defn paragraph?
  [zloc]
  (string? (source/sexpr zloc)))

(defn whitespace?
  [zloc]
  (node/whitespace-or-comment? (source/node zloc)))
