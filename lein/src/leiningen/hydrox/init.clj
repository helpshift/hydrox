(ns leiningen.hydrox.init
  (:require [rewrite-clj.zip :as zip]
            [rewrite-clj.node :as node]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(def +template+
  {:directories
   ["test/documentation"
    "template/assets/css"
    "template/assets/img"
    "template/assets/js"
    "template/partials"]
   :files
   ["test/documentation/sample_document.clj"
    "template/assets/css/rdash.min.css"
    "template/assets/css/scrollspy.css"
    "template/assets/img/favicon.png"
    "template/assets/img/logo.png"
    "template/assets/img/logo-white.png"
    "template/assets/js/angular-highlightjs.min.js"
    "template/partials/deps-web.html"
    "template/partials/navbar.html"
    "template/article.html"]})

(defn init [project]
  ;; copy files
  (doseq [dir (:directories +template+)]
    (fs/mkdirs (str (:root project) "/" dir)))
  (doseq [f (:files +template+)]
    (io/copy (io/file (io/resource (str "hydrox/" f)))
             (io/file (:root project) f)))

  ;; place sample entry in project.clj
  (let [proj (zip/of-file (str (:root project) "/project.clj"))]
    (if-not (zip/find-depth-first
             proj
             (comp #(= :documentation %) zip/sexpr))
      (-> proj
          zip/down
          zip/rightmost*
          (zip/insert-right (node/newline-node "\n"))
          zip/rightmost*
          (zip/insert-left (node/newline-node "\n"))
          (zip/prepend-space 1)
          (zip/insert-left (node/token-node :documentation ":documentation"))
          (zip/insert-left (-> (io/resource "hydrox/sample.edn")
                                (zip/of-file)
                                (zip/next)
                                (zip/node)))
          (zip/->root-string)
          (->> (spit (str (:root project) "/project.clj")))))))

(comment
  (io/copy (io/file (io/resource (str "hydrox/" "template/article.html")))
           (io/file "template/article.html"))
  
  )
