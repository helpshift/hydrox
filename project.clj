(defproject parkside-securities/hydrox "0.10.1-SNAPSHOT"
  :description "dive deeper into your code"
  :url "https://github.com/helpshift/hydrox"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.reader "1.3.2"]
                 [im.chit/jai  "0.2.12"
                  :exclusions [org.clojure/tools.reader rewrite-clj]]
                 [im.chit/hara.data      "2.2.17"]
                 [im.chit/hara.io.watch  "2.2.17"]
                 [im.chit/hara.common.watch  "2.2.17"]
                 [im.chit/hara.component "2.2.17"]
                 [im.chit/hara.concurrent.pipe "2.2.17"]
                 [im.chit/hara.event     "2.2.17"]
                 [im.chit/hara.string    "2.2.17"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.68"]
                 [stencil "0.5.0"]
                 [me.raynes/fs "1.4.6"]
                 [rewrite-clj "0.6.1"]]
  :documentation {:site   "hydrox"
                  :output "docs"
                  :template {:path "template"
                             :copy ["assets"]
                             :defaults {:template     "article.html"
                                        :navbar       [:file "partials/navbar.html"]
                                        :dependencies [:file "partials/deps-web.html"]
                                        :navigation   :navigation
                                        :article      :article}}
                  :paths ["test/documentation"]
                  :files {"sample-document"
                          {:input "test/documentation/sample_document.clj"
                           :title "a sample document"
                           :subtitle "generating a document from code"}
                          "index"
                          {:input "test/documentation/hydrox_guide.clj"
                           :title "hydrox"
                           :subtitle "dive deeper into your code"}}
                  :link {:auto-tag    true
                         :auto-number  true}}

  :profiles {:dev {:dependencies [[midje "1.9.7"
                                   :exclusions [org.clojure/tools.reader]]
                                  [compojure "1.4.0"
                                   :exclusions [org.clojure/tools.reader]]
                                  [ring "1.4.0"]
                                  [clj-http "3.9.1"]]
                   :plugins [[lein-midje "3.2.1"]
                             [lein-ancient "0.6.7"]
                             [lein-hydrox "0.1.14"]]}}
  :repositories [["clojar" {:url "https://clojars.org/repo"
                            :sign-releases false
                            :username [:gpg :env/clojar_username]
                            :password [:gpg :env/clojar_password]}]])
