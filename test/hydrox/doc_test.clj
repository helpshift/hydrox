(ns hydrox.doc-test
  (:use midje.sweet)
  (:require [hydrox.doc :as doc]
            [hydrox.core :as core]
            [clojure.java.io :as io]
            [hiccup.core :as html]))


(comment

  (clojure.lang.Namespace/findOrCreate 'a)
  (clojure.lang.Namespace/remove 'a)

  (doc/render-all @(:state reg))

  (def reg (let [proj  (core/read-project (io/file "../../chit/hara/project.clj"))
                 folio (-> proj
                           (core/create-folio)
                           (core/init-folio))
                 state (atom folio)]
             (core/regulator state proj)))
  
  

  (core/import-docstring (core/once-off "project.clj"))
  (core/import-docstring reg)
  
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
