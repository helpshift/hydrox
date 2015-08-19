(ns hydrox.doc.render.util-test
  (:use midje.sweet)
  (:require [hydrox.doc.render.util :refer :all]))

^{:refer hydrox.doc.render.util/adjust-indent :added "0.1"}
(fact "fixes indentation for code that is off slightly due to alignment"

  (adjust-indent "(+ 1\n     2)" 2)
  => "(+ 1\n   2)")

^{:refer hydrox.doc.render.util/basic-html-escape :added "0.1"}
(fact "escapes characters using standard html format"

  (basic-html-escape "<>")
  => "&lt;&gt;")

^{:refer hydrox.doc.render.util/basic-html-unescape :added "0.1"}
(fact "unescapes characters with standard html format"

  (basic-html-unescape "&amp;quot;")
  => "&quot;")

^{:refer hydrox.doc.render.util/markup :added "0.1"}
(fact "calls the markdown library to create markup from a string")


^{:refer hydrox.doc.render.util/join :added "0.1"}
(fact "like string/join but will return the input if it is a string"

  (join "hello") => "hello"

  (join ["hello" " " "world"]) => "hello world")
