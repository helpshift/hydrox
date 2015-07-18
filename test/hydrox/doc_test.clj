(ns hydrox.doc-test
  (:use midje.sweet)
  (:require [hydrox.doc :as doc]
            [hydrox.doc.render :as render]
            [hydrox.core :as core]
            [clojure.java.io :as io]
            [hiccup.core :as html]))

(def root (-> reg :project :root))
(def dopts (-> reg :project :documentation))

(defn template-full-path [path base project]
  (str (:root project) "/" base "/" path))

(defn filter-strings [m]
  (reduce-kv (fn [m k v] (if (string? v)
                           (assoc m k v)
                           m))
             {}
             (core/read-project (io/file "../hara/project.clj"))))

(defn find-includes [html]
  (->> html
       (re-seq #"<@=([^>^<]+)>")
       (map second)
       (map keyword)
       set))

(defn replace-template [template includes opts project]
  (reduce-kv (fn [html k v]
               (.replaceAll html (str "<@=" (name k) ">")
                            (cond (string? v)
                                  v
                                  
                                  (vector? v)
                                  (cond (= (first v) :file)
                                        (slurp (template-full-path (second v) (-> opts :template :path) project))))))
             template
             includes))

(defn replace-doc [name includes folio]
  (let [no-doc (->> (filter (fn [[k v]] (#{:article :navigation} v)) includes)
                    empty?)]
    (cond no-doc
          includes

          :else
          (let [elements (doc/generate folio name)]
            (reduce-kv (fn [out k v]
                         (assoc out k (case v
                                        :article    (render/render-article elements folio)
                                        :navigation (render/render-navigation elements folio)
                                        v)))
                       {}
                       includes)))))

(defn render-entry [name entry folio]
  (let [project        (:project folio)
        opts           (:documentation project)
        entry          (merge (filter-strings project) (-> opts :template :defaults) entry)
        template-path  (template-full-path (:template entry) (-> opts :template :path) project)
        output-path    (template-full-path (str name ".html") (:output opts) project)
        template       (slurp template-path)
        ks             (find-includes template)
        includes       (select-keys entry ks)
        includes       (replace-doc name includes folio)
        html           (replace-template template includes opts project)]
    (spit output-path html)))

(defn render-project [folio]
  (let [opts (-> folio :project :documentation)]
    (doseq [[name entry] (:files opts)]
      (println "Rendering" name)
      (render-entry name entry folio))))


(comment
  (render-project @(:state reg))
  (render-entry "index" {:template "home.html" :title "home"} (:project reg))





  (-> dopts :files (get "index")))

(comment

  (def reg (let [proj  (core/read-project (io/file "../hara/project.clj"))
                   folio (-> proj
                             (core/create-folio)
                             (core/init-folio))
                   state (atom folio)]
             (core/regulator state proj)))

  (swap! (:state reg)
         assoc :project (core/read-project (io/file "../hara/project.clj")))

  
  

(do (def skele (generate @(:state reg) "hara-concurrent-ova"))
    (-> (slurp "../hara/template/home.html")
        (.replaceAll "<@=title>"    "hara")
        (.replaceAll "<@=dependencies>"  (slurp "../hara/template/partials/deps-web.html"))
        (.replaceAll "<@=sidebar>"    (slurp "../hara/template/partials/sidebar.html"))
        (.replaceFirst "<@=footer>"    "")
        (->> (spit "../hara/docs/index.html"))))

(do (def skele (generate @(:state reg) "hara-concurrent-ova"))
    (-> (slurp "../hara/template/article.html")
        (.replaceAll "<@=title>"    "concurrent.ova")
        (.replaceAll "<@=subtitle>"  "shared mutable state for multi-threaded applications")
        (.replaceAll "<@=dependencies>"  (slurp "../hara/template/partials/deps-web.html"))
        (.replaceAll "<@=sidebar>"    (slurp "../hara/template/partials/sidebar.html"))
        (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
        (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
        (.replaceFirst "<@=footer>"    "")
        (->> (spit "../hara/docs/hara-concurrent-ova.html"))))

(do (def skele (generate @(:state reg) "hara-event"))
    (-> (slurp "../hara/template/article.html")
        (.replaceAll "<@=title>"    "hara.event")
        (.replaceAll "<@=subtitle>"  "event signalling and conditional restart framework")
        (.replaceAll "<@=dependencies>"  (slurp "../hara/template/partials/deps-web.html"))
        (.replaceAll "<@=sidebar>"    (slurp "../hara/template/partials/sidebar.html"))
        (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
        (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
        (.replaceFirst "<@=footer>"    "")
        (->> (spit "../hara/docs/hara-event.html")))
    )

  (do (def skele (generate @(:state reg) "hara-io-scheduler"))
      (-> (slurp "../hara-front/dash/src/template.html")
          (.replaceAll "<@=title>"    "hara.io.scheduler")
          (.replaceAll "<@=subtitle>"  "easy and intuitive task scheduling")
          (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
          (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
          (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
          (.replaceFirst "<@=footer>"    "")
          (->> (spit "../hara-front/dash/src/hara-io-scheduler.html"))))
 
  (do (def skele (generate @(:state reg) "hara-component"))
      (-> (slurp "../hara-front/dash/src/template.html")
          (.replaceAll "<@=title>"    "hara.component")
          (.replaceAll "<@=subtitle>"  "constructing composable systems")
          (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
          (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
          (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
          (.replaceFirst "<@=footer>"    "")
          (->> (spit "../hara-front/dash/src/hara-component.html"))))
  
  (do (def skele (generate @(:state reg) "hara-reflect"))
      (-> (slurp "../hara-front/dash/src/template.html")
          (.replaceAll "<@=title>"    "hara.reflect")
          (.replaceAll "<@=subtitle>"  "Java reflection made easy")
          (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
          (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
          (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
          (.replaceFirst "<@=footer>"    "")
          (->> (spit "../hara-front/dash/src/hara-reflect.html"))))
  
  (do (def skele (generate @(:state reg) "hara-event"))
      (-> (slurp "../hara-front/dash/src/template.html")
          (.replaceAll "<@=title>"    "hara.event")
          (.replaceAll "<@=subtitle>"  "event signalling and conditional restart framework")
          (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
          (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
          (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
          (.replaceFirst "<@=footer>"    "")
          (->> (spit "../hara-front/dash/src/hara-event.html")))
      (+ 1 1))

    (do (def skele (generate @(:state reg) "hara-io-scheduler"))
      (-> (slurp "../hara-front/dash/src/template.html")
          (.replaceAll "<@=title>"    "hara.io.scheduler")
          (.replaceAll "<@=subtitle>"  "easy and intuitive task scheduling")
          (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
          (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
          (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
          (.replaceFirst "<@=footer>"    "")
          (->> (spit "../hara-front/dash/src/hara-io-scheduler.html"))))

  (do (def skele (generate @(:state reg) "api"))
      (-> (slurp "../hara-front/dash/src/template.html")
          (.replaceAll "<@=title>"    "api")
          (.replaceAll "<@=subtitle>"  "reference code for all hara namespaces")
          (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
          (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
          (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
          (.replaceFirst "<@=footer>"    "")
          (->> (spit "../hara-front/dash/src/api.html")))
      )
  ""
  (def skele (generate @(:state reg) "logic"))
         ;(spit "logic.html" (-> (html/html (generate @(:state reg) "logic"))))

  (-> (slurp "../hara-front/dash/src/template.html")
             (.replaceAll "<@=title>"    "core.logic")
             (.replaceAll "<@=subtitle>"    "Logic programming for the absolute beginner")
             (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
             (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
             (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
             (.replaceFirst "<@=footer>"    "")
             (->> (spit "../hara-front/dash/src/logic.html")))

  
         
         

         )


(comment
  (./pull-project)
  )
