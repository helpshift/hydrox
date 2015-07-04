(ns documentation.on-lisp.ch2-functions
  (:use midje.sweet))

"Functions are the building-blocks of Lisp programs. They are also the building-blocks of Lisp. In most
languages the + operator is something quite different from user-defined functions. But Lisp has a single model,
function application, to describe all the computation done by a program. The Lisp + operator is a function,
just like the ones you can define yourself.

In fact, except for a small number of operators called special forms, the core of Lisp is a collection of Lisp
functions. What's to stop you from adding to this collection? Nothing at all: if you think of something you
wish Lisp could do, you can write it yourself, and your new function will be treated just like the built-in ones.

This fact has important consequences for the programmer. It means that any new function could be considered
either as an addition to Lisp, or as part of a specific application. Typically, an experienced Lisp programmer
will write some of each, adjusting the boundary between language and application until the two fit one another
perfectly. This book is about how to achieve a good fit between language and application. Since everything we
do toward this end ultimately depends on functions, functions are the natural place to begin."

[[:section {:title "Functions as Data"}]]

"Two things make Lisp functions different. One, mentioned above, is that Lisp itself is a collection of
functions. This means that we can add to Lisp new operators of our own. Another important thing to know about
functions is that they are Lisp objects.

Lisp offers most of the data types one finds in other languages. We get integers and floating-point numbers,
strings, arrays, structures, and so on. But Lisp supports one data type which may at first seem surprising: the
function. Nearly all programming languages provide some form of function or procedure. What does it mean to say
that Lisp provides them as a data type? It means that in Lisp we can do with functions all the things we expect
to do with more familiar data types, like integers: create new ones at runtime, store them in variables and in
structures, pass them as arguments to other functions, and return them as results.

The ability to create and return functions at runtime is particularly useful. This might sound at first like a
dubious sort of advantage, like the self-modifying machine language programs one can run on some computers. But
creating new functions at runtime turns out to be a routinely used Lisp programming technique."

[[:section {:title "Definding Functions"}]]

"Most people first learn how to make functions with `defun`. The following expression defines a function called
double which returns twice its argument."

(defn double [x] (* x 2))

"Having fed this to Lisp, we can call double in other functions, or from the toplevel:"

(fact

 > (double 1)
 2

 (+ 1 2)
 => 3)

"A file of Lisp code usually consists mainly of such defuns, and so resembles a file of procedure definitions
in a language like C or Pascal. But something quite different is going on. Those defuns are not just procedure
definitions, they're Lisp calls. This distinction will become clearer when we see what's going on underneath
defun.

Functions are objects in their own right. What defun really does is build one, and store it under the name
given as the first argument. So as well as calling double, we can get hold of the function which implements it.
The usual way to do so is by using the #' (sharp-quote) operator. This operator can be understood as mapping
names to actual function objects. By affixing it to the name of double"

(fact

  > (type #'double)
  clojure.lang.Var

  
  
  > ((fn [x] (* x 2)) 1)
  2)
