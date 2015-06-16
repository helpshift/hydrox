(ns example
  (:use clojure.test))

^{:refer clojure.core/+ :added "0.1"}
(deftest hello
  "We are generating stuff for hello"
  
  (is (= "hello" "there"))

  (is (instance? Class String)))


"We are generating stuff for hello

\"hello\" 
=> \"there\"

(instance? Class String) 
=> true
"

