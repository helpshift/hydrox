(ns documentation.hydrox-guide
  (:require [midje.sweet :refer :all]))

[[:chapter {:title "Introduction"}]]

"[hydrox](https://www.github.com/helpshift/hydrox) assists in the transmission of knowledge around a clojure project, providing in-repl management of documentation, docstrings and metadata through the reuse/repurposing of test code. Helping to create 'documentation that we can run', the tool allows for a design-orientated workflow for the programming process, blurring the boundaries between design, development, testing and documentation.

There are two main aspects of the tool that allow for this type of integrated workflow:

- management of function docstrings and metadata through tests
- generation of html documentation from tests
"

[[:image {:src "https://raw.githubusercontent.com/helpshift/hydrox/master/template/assets/img/hydrox-overview.png"}]]

[[:section {:title "Installation"}]]

"In your project.clj, add hydrox to the `[:profiles :dev :dependencies]` entry:

```clojure
(defproject ...
    ...
    :profiles {:dev {:dependencies [...
                                    [helpshift/hydrox \"{{PROJECT.version}}\"]
                                    ...]}}
    ...)
```
"

"All functionality is the `hydrox.core` namespace:"

(comment
  (use 'hydrox.core)

  
  (dive)    ;; initialises the tool, and watches project for files changes

  (import-docstring) ;; imports docstrings into functions from test files

  (purge-docstring)  ;; purges docstrings from functions

  (generate-docs)    ;; generates html (like this one) from test files

  (surface) ;; stops the tool
  )

[[:section {:title "Injection"}]]

"A better experience can be obtained by using [vinyasa](https://github.com/zcaudate/vinyasa) and editing your `~/.lein/profiles.clj` to inject all `hydrox` vars into the `.` namespace:

```clojure
{:user 
   {:plugins [...]        
    :dependencies [[im.chit/vinyasa.inject \"0.3.4\"]
                   [helpshift/hydrox \"{{PROJECT.version}}\"]]
    :injections 
    [(require '[vinyasa.inject :as inject])
     (inject/in [hydrox.core dive surface single-use 
                             generate-docs import-docstring purge-docstring])]}}
```
"

"Functions can then be used like this within the repl:"

(comment
  (./dive)    ;; initialises the tool, and watches project for files changes

  
  (./import-docstring) ;; imports docstrings into functions from test files

  (./purge-docstring)  ;; purges docstrings from functions

  (./generate-docs)    ;; generates html (like this one) from test files

  (./surface) ;; stops the tool
  )

[[:section {:title "Motivation"}]]
"The precessor of [hydrox](https://github.com/helpshift/hydrox) was [midje-doc](https://github.com/zcaudate/lein-midje-doc), and it provided very much the same functionalities as the current `hydrox` implementation. Whilst [midje-doc](https://github.com/zcaudate/lein-midje-doc) was built primarily as a leiningen plugin, it was found that the tool was more effective for the development when used within the repl.

There are significant improvements of [hydrox](https://github.com/helpshift/hydrox) over [midje-doc](https://github.com/zcaudate/lein-midje-doc) including:

- Declarative source code traversal using [jai](https://www.github.com/zcaudate/jai)
- Extensible micropass pipeline for compilation and linking
- Customisable templates system for html documentataion
- Code-diffing mechanism for efficient management of filesystem changes
"

[[:section {:title "Literate Programming"}]]

"
The phrase 'Literate Programming' has been bandied around alot lately. The main idea is that the code is written in a way that allows both a machine *and* a person to understand what is going on. Most seem to agree that it is a great idea. The reuse factor of not writing seperate documentation alone brings great excitement to many developers. However, we must understand that the methods of effective communication to humans and machines are very different:

- Communication to Machines are usually linear and based on a specific set of instructions. First Do This, Then Do That.... Machines don't really care what the code does. It just executes whatever code it has been given. The main importance of establishing that a program is correct is to give it a set of verifible `input`/`output` responses and see if it behaves a certain way.

- Communication to Humans are usually less procedural and more relational. We wish to be engaged, inspired and taught, not given a sequence of instructions that each break down to even smaller sequences. The best documentation are usally seperated into logical sections like an `overview`, `table of contents`, `list of figures`, `topic chapters`, `subsections`. There are `text`, `code`, `pictures`, even `sound` and `video`. Documentation structure resemble trees, with links between content that connect related topics and content. They do not resemble program code and therefore should be created independently of the machine code itself.
"

[[:section {:title "Facillitating Communication"}]]

"
The primary reason for building `hydrox` was to simplify the communication process across a clojure project. In general, the programming process can be seen as a set of different communication types with the developer acting as the facillitator for the following:

1. with the machine telling it what to do (code)
- with the machine, verifying that it has done it's job (unit and integration tests)
- with themselves/other developers, telling them how to use the function (unit tests and docstrings)
- with the end user, telling them how to do it (design/integration documentation)

Notice that with the 4 points listed, there happens to be some overlap between 2 and 3 (unit tests) as well as 2 and 4 (integration). We can categorise the overlap as follows:

- unit/function level documentation (intended for communicating with a machine/developer)
- design/integration level documentation (intended for communicating with a consumer)

The effective communication for each of these is very different and should be seperately managed.
"

[[:section {:title "Functional Level Documentation"}]]




[[:section {:title "Design Level Documentation"}]]

""



"The best for of "





[[{:tag "qs-project" :title "project.clj"}]]
(comment
  (defproject ...
    ...
    :profiles {:dev {:dependencies [[midje "1.5.1"]]}}
    ...
    :documentation
    {:files {"<document-name>"             ;; my-first-document
             {:input "<input-file-path>"   ;; test/docs/my_first_document.clj
              :title "<title>"             ;; My First Document
              :sub-title "<sub title>"     ;; Learning how to use midje-doc
              :author "<name>"
              :email  "<email>"}}}
    ...))

[[{:tag "qs-first-doc" :title "test/docs/my_first_document.clj"}]]

(comment
  (ns docs.my-first-document
    (:require [midje.sweet :refer :all]))

  [[:chapter {:tag "hello" :title "Hello Midje Doc"}]]

  "This is an introduction to writing with midje-doc."

  [[:section {:title "Defining a function"}]]

  "We define function `add-5`"

  [[{:numbered false}]]
  (defn add-5 [x]
    (+ x 5))

  [[:section {:title "Testing a function"}]]

  "`add-5` outputs the following results seen in
 [e.{{add-5-1}}](#add-5-1) and [e.{{add-5-10}}](#add-5-10):"

  (facts
    [[{:tag "add-5-1" :title "1 add 5 = 6"}]]
    (add-5 1) => 6

    [[{:tag "add-5-10" :title "10 add 5 = 15"}]]
    (add-5 10) => 15))



[[:chapter {:title "Programming vs Documentation"}]]


[[:chapter {:title "Tooling for Documents"}]]

[[:section {:title "Documentation Bugs"}]]
"
Programming is a very precise art form. Programming mistakes, especially the little
ones, can result in dire consequences and much wasted time. We therefore use tools
such as debuggers, type checkers and test frameworks to make our coding lives
easier and our source code correct.

Documentation is the programmers' means of communicating how to use or build upon a library, usually to a larger audience of peers. This means that any mistakes in the documentation results in
wasted time for *all involved*. Therefore any mistake in documentation can have a
*greater effect* than a mistake in source code because it wastes *everybody's* time.

There are various tools for documentation like `latex`, `wiki` and `markdown`. However, none address the issue that code examples in the documentation are not checked for correctness. A fictitious example illustrates how errors in documentation can produced and propagated can be seen in [chapter {{a-bugs-life}}](#a-bugs-life)
"

[[:section {:title "Test Cases vs Docstrings"}]]

"
The best description for our functions are not found in *source files* but in the *test files*. Test files *are* potentially the best documentation because they provide information about what a function outputs, what inputs it accepts and what exceptions it throws. Instead of writing vague phrases ([e.{{split-source}}](#split-source)) in the doc-string, we can write the descriptions of what a function does directly with our tests ([e.{{split-tests}}](#split-tests)).
"

[[{:tag "split-source" :title "source code (how to do something)"}]]
(defn split-string
  "The split-string function is used to split a string
  in two according to the idx that is passed."
  [s idx]
  [(.substring s 0 idx) (.substring s idx)])

[[{:tag "split-tests" :title "test code (how something is used)"}]]

(comment
  (facts "split-string usage:"

    (split-string "abcde" 1)
    => ["a" "bcde"]

    (split-string "abcde" 3)
    => ["abc" "de"]))

"It can be seen that test cases provides a much better explaination for `split-string` than the source doc-string. The irony however is that when a readme says: *`'for documentation, please read the test files'`*, the common consensus is that the project developer is too slack to write proper documentation. However, if we are truly honest with our own faults, this occurs because most programmers are too slack to read tests. **We only want to read pretty documentation.**"

[[:section {:title "Bridging the Divide"}]]

"`lein-midje-doc` plugin attempts to bridge the gap between writing tests and writing documentation by introducing three novel features:"

[[:subsection {:title "Features"}]]

"The features are:
 1. To generate `.html` documentation from a `.clj` test file.
 2. To express documentation elements as clojure datastructures.
 3. To render clojure code and midje facts as code examples.
 4. To allow tagging of elements for numbering and linking.
 "

[[:subsection {:title "Benefits"}]]
"In this way, the programmer as well as all users of the library benefits:

 1. All documentation errors can be eliminated.
 2. Removes the need to cut and copy test examples into a readme file.
 3. Entire test suites can potentially be turned into nice looking documentation with relatively little work.
 "

;;[[:file {:src "test/midje_doc/api.clj"}]]

;;[[:file {:src "test/midje_doc/bug_example.clj"}]]

[[:chapter {:title "End Notes"}]]

"For any feedback, requests and comments, please feel free to lodge an issue on github or contact me directly.

Chris.
"
