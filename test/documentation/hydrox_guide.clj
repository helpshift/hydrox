(ns documentation.hydrox-guide
  (:require [midje.sweet :refer :all]))

[[:chapter {:title "Introduction"}]]

[[:section {:title "Overview"}]]

"[hydrox](https://www.github.com/helpshift/hydrox) assists in the transmission of knowledge around a clojure project, providing in-repl management of documentation, docstrings and metadata through the reuse/repurposing of test code. Facillitating the creation of 'documentation that we can run', the tool allows for a design-orientated workflow for the programming process, blurring the boundaries between design, development, testing and documentation."

[[:image {:src "https://raw.githubusercontent.com/helpshift/hydrox/master/template/assets/img/hydrox-overview.png" :width "100%" :title "Overview"}]]

[[:section {:title "Installation"}]]

[[:subsection {:title "Standard"}]]

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

[[:subsection {:title "Vinyasa"}]]

"A better experience can be obtained by using [vinyasa](https://github.com/zcaudate/vinyasa) and editing your `~/.lein/profiles.clj` to inject all `hydrox` vars into the `.` namespace:

```clojure
{:user
   {:plugins [...]
    :dependencies [[im.chit/vinyasa.inject \"0.3.4\"]
                   [helpshift/hydrox \"{{PROJECT.version}}\"]]
    :injections
    [(require '[vinyasa.inject :as inject])
     (inject/in [hydrox.core dive surface generate-docs
                             import-docstring purge-docstring])]}}
```
"

"Functions can then be used like this within the repl:"

(comment
  (./dive)


  (./import-docstring)

  (./purge-docstring)

  (./generate-docs)

  (./surface))

[[:section {:title "Motivation"}]]
"Programming is a very precise art form. Programming mistakes, especially the little ones, can result in dire consequences and much wasted time. We therefore use tools such as debuggers, type checkers and test frameworks to make our coding lives easier and our source code correct.

Documentation is the programmers' means of communicating how to use or build upon a library, usually to a larger audience of peers. This means that any mistakes in the documentation results in wasted time for *all involved*. Therefore any mistake in documentation can have a
*greater effect* than a mistake in source code because it wastes *everybody's* time.

There are various tools for documentation like `latex`, `wiki` and `markdown`. However, none address the issue that code examples in the documentation are not checked for correctness. `hydrox` attempts to bridge the gap between writing tests and writing documentation by introducing the following features:"

[[:subsection {:title "Features"}]]

"The features are:

1. To generate `.html` documentation from a `.clj` test file.
-  Management of function docstrings and metadata through tests
-  Express documentation elements as clojure datastructures.
-  Render code, clojure.test and midje test cases as examples.
-  Latex-like numbering and linking facilities.
"

[[:subsection {:title "Benefits"}]]
"In this way, the project benefits in multiple ways:

1. All documentation errors can be eliminated.
- Removes the need to cut and copy test examples into a readme file.
"

[[:subsection {:title "Improvements"}]]

"The precessor of [hydrox](https://github.com/helpshift/hydrox) was [midje-doc](https://github.com/zcaudate/lein-midje-doc), and it provided very much the same functionalities as the current `hydrox` implementation. Whilst [midje-doc](https://github.com/zcaudate/lein-midje-doc) was built primarily as a leiningen plugin, it was found that the tool was more effective for the development when used within the repl.

There are significant improvements of [hydrox](https://github.com/helpshift/hydrox) over [midje-doc](https://github.com/zcaudate/lein-midje-doc) including:

- Declarative source code traversal using [jai](https://www.github.com/zcaudate/jai)
- Extensible micropass pipeline for compilation and linking
- Customisable templates system for html documentataion
- Code-diffing mechanism for efficient management of filesystem changes
"

[[:section {:title "Literate Programming"}]]

"The phrase 'Literate Programming' has been bandied around alot lately. The main idea is that the code is written in a way that allows both a machine *and* a person to understand what is going on. Most seem to agree that it is a great idea. The reuse factor of not writing seperate documentation alone brings great excitement to many developers. However, we must understand that methods of effective communication to humans and machines are very different:

**Communication to Machines** are usually linear and based on a specific set of instructions. First Do This, Then Do That.... Machines don't really care what the code does. It just executes whatever code it has been given. The main importance of establishing that a program is correct is to give it a set of verifible `input`/`output` responses and see if it behaves a certain way.

**Communication to Humans** are usually less procedural and more relational. We wish to be engaged, inspired and taught, not given a sequence of instructions that each break down to even smaller sequences. The best documentation are usally seperated into logical sections like an `overview`, `table of contents`, `list of figures`, `chapters`, `sections` and `subsections`. There are `text`, `code`, `pictures`, even `sound` and `video`. Documentation structure resemble trees, with links between content that connect related topics and content. They do not resemble program code and therefore should be created independently of the machine code itself.
"

[[:chapter {:title "Facillitating Communication"}]]

[[:section {:title "Code as Communication"}]]
"
The primary reason for building `hydrox` was to simplify the communication process across a clojure project. In general, the programming process can be seen as a set of different communication types with the developer acting as the facillitator for the following:

1. with the machine telling it what to do (code)
- with the machine, verifying that it has done it's job (unit and integration tests)
- with themselves/other developers, telling them how to use the function (unit tests and docstrings)
- with the end user, telling them how to do it (design/integration documentation)

Notice that with the 4 points listed, there happens to be some overlap between 2 and 3 (unit tests) as well as 2 and 4 (integration). We can categorise the overlap as follows:

1. unit/function level documentation (intended for communicating with a machine/developer)
- design/integration level documentation (intended for communicating with a consumer)

This overlap is very important because this is the one area where repetitive activities occur. [hydrox](https://www.github.com/helpshift/hydrox) helps limit this repetition through automation of the documentation workflow."

[[:section {:title "Functional Level Documentation"}]]

"The best description for our functions are not found in *source files* but in the *test files*. Tests **are** potentially the best documentation because they provide information about what a function outputs, what inputs it accepts and what exceptions it throws. The best place to display functional level documentation is in the docstring as the clojure ecosystem has amazing support for the uses of docstings in tools such as cider, cursive, codox, grimoire and many others."

"Lets discuss the problem with the following piece of code:"

(defn split-string
  "The split-string function is used to split a string
  in two according to the idx that is passed."
  [s idx]
  [(.substring s 0 idx) (.substring s idx)])

"Technically, everything is fine except that the docstring is pretty much useless. Instead of writing vague phrases, we write our test as follows (usually in another file):
"
(comment
  ^{:refer documentation.hydrox-guide/split-string :added "0.1"}
  (facts "split-string usage:"

  (split-string "abcde" 1)
  => ["a" "bcde"]

  (split-string "abcde" 3)
  => ["abc" "de"]))

"It can be seen that test cases provides a much better explaination for `split-string` than the source docstring because any developer can immediately tell what inputs the function accepts and what outputs it returns. In this case, [hydrox](https://github.com/helpshift/hydrox) provides the function `import-docstring` to turn a test into a docstring and then deliver it back into the source code."

[[:subsection {:title "Import Docstring"}]]

"The following hydrox calls:"

(comment
  (hydrox.core/dive)
  (hydrox.core/import-docstring))

"Results in the `split-string` function looking like this:"

(defn split-string
  "split-string usage:

   (split-string \"abcde\" 1)
   => [\"a\" \"bcde\"]

   (split-string \"abcde\" 3)
   => [\"abc\" \"de\"]"
  {:added "0.1"}
  [s idx]
  [(.substring s 0 idx) (.substring s idx)])

"It is possible to target the docstring of a source file when the test code is in another file is due to the
`^{:refer documentation.hydrox-guide/split-string :added \"0.1\"}` metadata that was included as part of the test. The other call to `dive` was needed to link the source and test files. It only needs to called once and will be explained in a later section."
""
[[:subsection {:title "Purge Docstring"}]]

"Sometimes, it's better to just concentrate on the function itself for more clarity, in this case, the `purge-docstring` function is really useful:"

(comment
  (hydrox.core/purge-docstring))

"Results in the `split-string` function looking like this, having no docstring or metadata:"

(defn split-string
  [s idx]
  [(.substring s 0 idx) (.substring s idx)])

"To bring the docstring and metadata back again, just make another call to `import-docstring`. With the tests for `split-string` in place, it is easy to manage metadata and docstrings. Having the information within the test files decreases source code clutter and we can import/purge them as needed, dependending upon the situation."

[[:section {:title "Design Level Documentation"}]]

"Documentation at the design level requires more visual elements than documentation at the function level. [hydrox](https://www.github.com/helpshift/hydrox) can generate html output based on a `.clj` file and a template. This requires some configuration and so the following is placed in the `project.clj` map."

(comment
  (defproject ...
    ...
    :documentation
    {:site   "sample"
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
              :subtitle "generating a document from code"}}}
    ...))

"The `:documentation` key in `defproject` specifies which files to use as entry points to use for html generation. A sample can be seen below:"

(comment
  (ns documentation.sample.document
    (:require [midje.sweet :refer :all]))

  [[:chapter {:tag "hello" :title "Hello Midje Doc"}]]

  "This is an introduction to writing with midje-doc."

  [[:section {:title "Defining a function"}]]

  "We define function `add-5` below:"

  [[{:numbered false}]]
  (defn add-5 [x]
    (+ x 5))

  [[:section {:title "Testing a function"}]]

  "`add-5` outputs the following results:"

  (facts
    [[{:tag "add-5-1" :title "1 add 5 = 6"}]]
    (add-5 1) => 6

    [[{:tag "add-5-10" :title "10 add 5 = 15"}]]
    (add-5 10) => 15))

"A pretty looking html document can be generated by running `generate-docs`:"

(comment
  (hydrox.core/generate-docs))

"The `:output` entry specifies the directory that files are rendered to, `:template`' specifies the files that are needed to generate the file. The current template will generate the same as the current documentation."

[[:file {:src "test/documentation/hydrox_guide/api.clj"}]]

[[:file {:src "test/documentation/hydrox_guide/bug_example.clj"}]]
