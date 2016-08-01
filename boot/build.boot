(set-env!
  :resource-paths #{"src"}
  :dependencies   '[[org.clojure/clojure "1.6.0"       :scope "provided"]
                    [boot/core           "2.1.2"       :scope "provided"]
                    [adzerk/bootlaces    "0.1.11"      :scope "test"]
                    [helpshift/hydrox    "0.1.16"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.17-SNAPSHOT")

(bootlaces! +version+)

(task-options!
  pom {:project     'boot-hydrox
       :version     +version+
       :description "Provides hydrox page generation as Boot task"
       :url         "https://github.com/helpshift/hydrox"
       :scm         {:url "https://github.com/helpshift/hydrox"}
       :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask dev
  "Dev process"
  []
  (comp
    (watch)
    (repl :server true)
    (pom)
    (jar)
    (install)))
