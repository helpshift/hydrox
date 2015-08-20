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
                                  [helpshift/hydrox "0.1.0"]
                                  ...]}}
  ...)
```

## Usage

[hydrox](https://www.github.com/helpshift/hydrox) provides in-repl management of documentation, doc-string and function metadata, allowing for a design-orientated workflow for the programming process, blurring the boundaries between design, development, testing and documentation.

Please see the [docs](http://helpshift.github.io/hydrox) for more information.

## License

Copyright © 2015 Helpshift

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
