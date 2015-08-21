# hydrox

[![Build Status](https://travis-ci.org/helpshift/hydrox.svg?branch=master)](https://travis-ci.org/helpshift/hydrox)

dive deeper into your code

![hydrox logo](https://raw.githubusercontent.com/helpshift/hydrox/master/template/assets/img/big.png)

## Installation

In your `project.clj`, add hydrox to the `[:profiles :dev :dependencies]` entry:  

```clojure
(defproject ...
  ...
  :profiles {:dev {:dependencies [...
                                  [helpshift/hydrox "0.1.1"]
                                  ...]}}
  ...)
```

## Usage

[hydrox](https://www.github.com/helpshift/hydrox) assists in the transmission of knowledge around a clojure project, providing in-repl management of documentation, docstrings and metadata through the reuse/repurposing of test code. The tool allows for a design-orientated workflow for the programming process, blurring the boundaries between design, development, testing and documentation.

![hydrox overview](https://raw.githubusercontent.com/helpshift/hydrox/master/template/assets/img/hydrox-overview.png)

Please see the [docs](http://helpshift.github.io/hydrox) for more information (generated using itself).

## Links and References:

- the website for [hara](https://www.github.com/zcaudate/hara) has been generated using [hydrox](https://www.github.com/helpshift/hydrox)

- the (output)[http://helpshift.github.io/hydrox/sample-document.html] can be seen for a sample [input](https://github.com/helpshift/hydrox/blob/master/test/documentation/sample_document.clj)

## License

Copyright © 2015 [Helpshift](https://www.helpshift.com/)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
