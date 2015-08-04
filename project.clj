(defproject helpshift/hydrox "0.1.0-SNAPSHOT"
  :description "dive deeper into your code"
  :url "https://github.com/helpshift/hydrox"
  :license {:name "MIT"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [im.chit/jai  "0.2.5"]
                 [im.chit/hara "2.2.5"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.66"]
                 [stencil "0.3.5"]
                 [me.raynes/conch "0.8.0"]
                 ;;[im.chit/gita "0.1.1-SNAPSHOT"]
                 ;;[im.chit/adi  "0.3.2-SNAPSHOT"]
                 ;;[com.datomic/datomic-free "0.9.5173" :exclusions [joda-time]]
                 ]
  :profiles {:dev {:dependencies [[midje "1.7.0"]
                                  [compojure "1.4.0"]
                                  [ring "1.4.0"]
                                  [clj-http "1.1.2"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-ancient "0.6.7"]]}})
