(ns documentation.logic-tut.index
  (:use midje.sweet))

[[:chapter {:title "Introduction"}]]

"This tutorial will guide you through the magic and fun of combining relational programming
(also known as logic programming) with functional programming. This tutorial does not assume
that you have any knowledge of Lisp, Clojure, Java, or even functional programming. The only
thing this tutorial assumes is that you are not afraid of using the command line and you have
used at least one programming language before in your life."

[[:section {:title "Why Logic Programming?"}]]

"What's the point of writing programs in the relational paradigm?

First off, aesthetics dammit.

Logic programs are simply beautiful as they often have a declarative nature which trumps
even the gems found in functional programming languages. Logic programs use search, and thus
they are often not muddied up by algorithmic details. If you haven't tried Prolog before,
relational programming will at times seem almost magical. However, I admit, the most important
reason to learn the relational paradigm is because it's FUN."

[[:section {:title "Getting Started"}]]

"
1. Install `lein` following [instructions here](http://leiningen.org/#install)
- `git clone https://github.com/swannodette/logic-tutorial && cd logic-tutorial`

Ok, we're ready to begin. Type `lein repl`, which will drop you into the Clojure prompt.
First let's double check that everything went ok. Enter the following at the Clojure REPL:
"

(require 'clojure.core.logic)

"
The REPL should print `nil` and it should return control to you. If it doesn't file an
issue for this tutorial and I'll look into it. If all goes well run the following:
"

(load "logic_tutorial/tut1")

"
You'll see some harmless warnings, then run the following:
"

(in-ns 'logic-tutorial.tut1)

"Your prompt will change and you're now working in a place that has the magic of relational
programming available to you. The REPL prompt will show `logic-tutorial.tut1`, we're going
show `tut1` to keep things concise."

[[:chapter {:title "Exploration"}]]

"Unlike most programming systems, with relational programming we can actually ask the
computer questions. But before we ask the computer questions, we need define some facts! The
first thing we want the computer to know about is that there are men:"

(fact
  (db-rel man x)
  => #'man)

"And then we want to define some men:"

(def men
  (db [man 'Bob]
      [man 'John]))

[[:section {:title "Question and Answer"}]]

"Now we can ask who are men. Questions are always asked with `run` or `run*`. By convention
we'll declare a logic variable `q` and ask the computer to give use the possible values for
`q`. Here's an example:"

(fact
  (with-db men
          (run 1 [q] (man q)))
  => '(John))

[[:subsection {:title "Multiple Results"}]]

"We're asking the computer to give us at least one answer to the question - Who is a man?.
We can ask for more than one answer:"

(fact
  (with-db men
          (run 2 [q] (man q)))
  => '(John Bob))

"Now that is pretty cool. What happens if we ask for even more answers?"

(fact
  (with-db men
          (run 3 [q] (man q)))
 => '(John Bob))

"The same result. That's because we’ve only told the computer that two men exist in the
world. It can't give results for things it doesn't know about."

[[:section {:title "Fun Guys"}]]

"Let's define another kind of relationship and a fact:"

(fact
 (db-rel fun x)
 => #'fun)

(def fun-people
  (db [fun 'Bob]))

"Let's ask a new kind of question:"

(fact
  (with-dbs [men fun-people]
           (run* [q]
             (man q)
             (fun q)))
  => '(Bob))
