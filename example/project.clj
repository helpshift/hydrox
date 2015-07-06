(defproject example "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hara "2.2.0-SNAPSHOT"]]
  :documentation {:type   :portfolio
                  :name   "example"
                  :output "doc"
                  :description "code patterns and utilities"
                  :tracking "UA-31320512-2"
                  :owners [{:name    "Chris Zheng"
                            :email   "z@caudate.me"
                            :website "http://z.caudate.me"}]
                  :paths ["test/documentation"]
                  :files {"home"
                          {:input "test/documentation/example/home.clj"}
                          "logic"
                          {:input "test/documentation/logic_tut/index.clj"
                           :title "Relational and Logic Programming"}
                          "on-lisp"
                          {:input "test/documentation/on_lisp/book.clj"}
                          "quickstart"
                          {:input "test/documentation/example/quickstart.clj"}
                          "component"
                          {:input "test/documentation/example/component.clj"}
                          "watch"
                          {:input "test/documentation/example/watch.clj"}
                          "scheduler"
                          {:input "test/documentation/example/scheduler.clj"}}
                  :html {:logo  "hara.png"
                         :theme "clean"
                         :home  "home"
                         :navigation ["quickstart"
                                      "on-lisp"
                                      ["guides" ["component"
                                                 "ova"
                                                 "watch"
                                                 "scheduler"]]
                                      {:link "api", :text "api"}
                                      {:link "https://gitter.im/zcaudate/hara",
                                       :text "support"}
                                      {:link "https://www.github.com/zcaudate/hara",
                                       :text "source"}]}
                  :link {:auto-tag    true
                         :auto-number true}})
