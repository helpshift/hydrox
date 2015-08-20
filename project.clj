(defproject helpshift/hydrox "0.1.0"
  :description "dive deeper into your code"
  :url "https://github.com/helpshift/hydrox"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [im.chit/jai  "0.2.7"]
                 [im.chit/hara "2.2.7"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.66"]
                 [stencil "0.3.5"]
                 [me.raynes/fs "1.4.6"]]
  :documentation {:site   "hydrox"
                  :output "docs"
                  :description "dive deeper into your code"
                  :tracking "UA-31320512-2"
                  :owners [{:name    "Chris Zheng"
                            :email   "chris@helpshift.com"
                            :website "http://www.helpshift.com"}]
                  :template {:path "template"
                             :copy ["assets"]
                             :defaults {:template     "article.html"
                                        :navbar       [:file "partials/navbar.html"]
                                        :dependencies [:file "partials/deps-web.html"]
                                        :contentbar   :navigation
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
                       :auto-number true}}

  :profiles {:dev {:dependencies [[midje "1.7.0"]
                                  [compojure "1.4.0"]
                                  [ring "1.4.0"]
                                  [clj-http "1.1.2"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-ancient "0.6.7"]]}})
