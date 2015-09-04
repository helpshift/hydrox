(ns hydrox.meta-test
  (:use midje.sweet)
  (:require [hydrox.meta :refer :all]))

^{:refer hydrox.meta/selector :added "0.1"}
(fact "builds a selector for functions"

  (selector 'hello)
  => '[(#{defn defmacro defmulti} | hello ^:%?- string? ^:%?- map? & _)])

^{:refer hydrox.meta/edit-file :added "0.1"}
(fact "helper function for file manipulation used by import and purge")

^{:refer hydrox.meta/import-fn :added "0.1"}
(fact "helper function for file import")

^{:refer hydrox.meta/import-var :added "0.1"}
(fact "import docs for a single var")

^{:refer hydrox.meta/import-file :added "0.1"}
(fact "import docs for a file")

^{:refer hydrox.meta/import-project :added "0.1"}
(fact "import docs for the entire project")

^{:refer hydrox.meta/purge-fn :added "0.1"}
(fact "helper function for file purge")

^{:refer hydrox.meta/purge-var :added "0.1"}
(fact "purge docs for a single var")

^{:refer hydrox.meta/purge-file :added "0.1"}
(fact "purge docs for a file")

^{:refer hydrox.meta/purge-project :added "0.1"}
(fact "purge docs for the entire project")


(comment
  (purge-file "src/hydrox/code.clj")

  (import-file "src/hydrox/code.clj"
               {'hydrox.meta {'import-var {:docs [(node/string-node "Hello there")]
                                           :meta {:added "0.1"}}}})
  (import-var "src/hydrox/code.clj"
              'import-var
              {'hydrox.meta {'import-var {:docs [(node/string-node "Hello there")]
                                          :meta {:added "0.1"}}}})

  (def z (source/of-file "src/hydrox/code.clj")))
