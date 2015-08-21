(ns documentation.hydrox-guide.bug-example
  (:require [midje.sweet :refer :all]))

[[:chapter {:title "A Bug's Life"}]]

[[:section {:title "Version One"}]]

"A new clojure project is created."

[[{:lang "bash" :numbered false}]]
(comment
> lein new fami
> cd fami
> lein repl)

"
A very useful function `add-5` has been defined:"

[[{:tag "add-5-fn" :title "src/fami/operations.clj"}]]
(comment
  (ns fami.operations)

  (defn add-5
    "add-5 is a function that takes any number of arguments
     and adds 5 to the sum"
    [& ns]
    (apply + 5 ns)))

"And the corresponding tests specified:"

[[{:tag "add-5-tests" :title "src/fami/test-operations.clj"}]]
(comment
  (ns fami.test-operations
    (:require [fami.operations :refer :all]
              [midje.sweet :refer :all]))

  (fact "add-5 should increment any list of numbers by 5"
    (add-5 5)  => 10
    (add-5 1 2 3 4) => 15))

"There are additional entries for the operation in the readme as well as also being scattered around in various other documents."
[[{:tag "add-5-readme" :title "readme.md, operations.md"}]]
(comment
  ...

  Here are some of the use cases for add-5

  (add-5 5)    ;; => 10
  (add-5 1 2 3 4)   ;; => 15
  
  ...)

"This version of this library has been released as **version 1.0**"

[[:section {:title "Version Two"}]]

"The library is super successful with many users. The code undergoes refactoring and it is decided that the original `add-5` is too powerful and so it must be muted to only accept one argument. An additional function `add-5-multi` is used to make explicit that the function is taking multiple arguments."

[[{:tag "add-5-v2" :title "src/fami/operations.clj - v2"}]]
(comment
  (ns fami.operations)

  (defn add-5 [n]   ;; The muted version
    "add-5 is a function that takes a number and adds 5 to it"
    (+ n 5))

  (defn add-5-multi
    "add-5-multi is a function that takes any number of arguments
     and adds 5 to the sum"
    [& ns]
    (apply + 5 ns)))

"The tests throw an exception but are quickly fixed:"

[[{:tag "add-5-v2-tests" :title "src/fami/test-operations.clj"}]]
 (comment
 (ns fami.test-operations
   (:require [fami.operations :refer :all]
             [midje.sweet :refer :all]))

 (fact "add-5 should increment only one input by 5"
   (add-5 5) => 10
   (add-5 1 2) => (throws clojure.lang.ArityException))

 (fact "add-5-multi should increment any list of numbers by 5"
   (add-5-multi 1 2 3 4) => 15))

"This version of this library has been released as **version 2.0**"


[[:section {:title "The Bug Surfaces"}]]

"Although the tests are correct, the documentation is not. Anyone using this library can potentially have the `clojure.lang.ArityException` bug if they carefully followed instructions in the documentation.

This is a trival example of a much greater problem. When a project begins to evolve and codebase begins to change, the documentation then becomes incorrect. Although source and test code can be isolated through testing, fixing documentation is a miserable and futile exercise of cut and paste. With no real tool to check whether code is still valid, the documentation become less and less correct until all the examples have to be rechecked and the documention rewritten.

**Then the codebase changes again ...**

Once the library has been release to the world and people have already started using it, there is no taking it back. Bugs propagate through miscommunication. Miscommunication with machines can usually be contained and fixed. Miscommunication with people becomes potentially more difficult to contain.

This simple scenario highlights the problem that developers have when updating documentation. When we cannot `prove` that our documentation is correct, then the cost of fixing out of date documentation becomes bigger and bigger as time progresses. With `hydrox` this process just became much more cost effective."
