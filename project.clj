(defproject im.chit/nitrox "0.1.0"
  :description "dive deeper into your code"
  :url "https://github.com/zcaudate/nitrox"
  :license {:name "MIT"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [im.chit/jai  "0.2.5"]
                 [im.chit/hara "2.1.12"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.66"]
                 [stencil "0.3.5"]
                 [me.raynes/conch "0.8.0"]]
   :profiles {:dev {:dependencies [[midje "1.6.3"]
                                   [leiningen "2.5.1"]]
                    :plugins [[lein-midje "3.1.3"]
                              [lein-ancient "0.6.7"]]}})