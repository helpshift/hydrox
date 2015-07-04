(ns documentation.short.t6-stencil
  (:require [example.core]))

[[:chapter {:title "Hello There" :tag "hello"}]]

[[:section {:title "Hello World" :tag "world"}]]

"{{hello}} {{stencil/world}} {{PROJECT.version}}"
