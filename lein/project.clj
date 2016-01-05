(defproject lein-hydrox "0.1.7"
  :description "leiningen plugin for hydrox"
  :url "https://github.com/helpshift/hydrox"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[helpshift/hydrox "0.1.7"]]
  :eval-in-leiningen true
  :documentation {:site   "sample"
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
                           :subtitle "generating a document from code"}}
                  :link {:auto-tag    true
                         :auto-number  true}})
