(ns nitrox.doc-test
  (:use midje.sweet)
  (:require [nitrox.doc :refer :all]
            [nitrox.doc.render :as render]
            [nitrox.core :as core]
            [clojure.java.io :as io]
            [hiccup.core :as html]))


(def reg (let [proj  (core/read-project (io/file "../hara/project.clj"))
                   folio (-> proj
                             (core/create-folio)
                             (core/init-folio))
                   state (atom folio)]
               (core/regulator state proj)))

(comment

  (do (def skele (generate @(:state reg) "hara-concurrent-ova"))
      (-> (slurp "../hara-front/dash/src/template.html")
          (.replaceAll "<@=title>"    "hara.concurrent.ova")
          (.replaceAll "<@=subtitle>"  "shared mutable state for multi-threaded applications")
          (.replaceAll "<@=sidebar>"    (slurp "../hara-front/dash/src/sidebar.html"))
          (.replaceFirst "<@=navbar>"   (render/render-navbar skele @(:state reg)))
          (.replaceFirst "<@=article>"  (render/render-article skele @(:state reg)))
          (.replaceFirst "<@=footer>"    "")
          (->> (spit "../hara-front/dash/src/hara-concurrent-ova.html"))))

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
