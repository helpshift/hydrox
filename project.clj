(defproject im.chit/nitrox "0.1.0-SNAPSHOT"
  :description "dive deeper into your code"
  :url "https://github.com/zcaudate/nitrox"
  :license {:name "MIT"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [im.chit/jai  "0.2.5"]
                 [im.chit/hara "2.2.0-SNAPSHOT"]
                 [im.chit/gita "0.1.1-SNAPSHOT"]
                 [im.chit/adi  "0.3.2-SNAPSHOT"]
                 [com.datomic/datomic-free "0.9.5173" :exclusions [joda-time]]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.66"]
                 [stencil "0.3.5"]
                 [leiningen "2.5.1"]
                 [me.raynes/conch "0.8.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-ancient "0.6.7"]]}})
