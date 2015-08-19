# hydrox

[![Build Status](https://travis-ci.org/helpshift/hydrox.svg?branch=master)](https://travis-ci.org/helpshift/hydrox)

dive deeper into your code

## Usage

  this is just a brain dump of the functionality that will be availibale for hydrox

  - so the first functionality will be being able to watch a project for changes

  - The second is to be able to take these changes and then put them into some sort of a dynamic database-backed storage mechanism using datomic/adi

  - The third is to be able to use the database to generate code

  - Create a .hydrox directory
              - meta.edn
              - <project>.datomic


  - Import the saved git versions of the project and to be able to generate versions
  - Every save creates a version that is attached to the current git sha of the project for amendments


  API

  (./hydrox)
  - updates git logs
  - updates git

  ;; basically creates everything from the project file

  (./doc-import :all)
  (./doc-import <ns>)
  (./doc-import <ns/var>)
  (./doc-import <file>)

  (./doc-purge  :all)
  (./doc-purge  <file>)
  (./doc-purge  <ns>)
  (./doc-purge  <ns/var>)

  (./doc-source <ns/var> <version>)
  (./doc-test   hydrox.core/query "0.2.1")

  (./doc-gen)
  (./doc-gen "0.3.1") for a particular version)

## License

Copyright © 2015 Chris Zheng

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
