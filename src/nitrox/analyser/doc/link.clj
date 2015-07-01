(ns nitrox.analyser.doc.link
  (:require [hara.namespace.import :as ns]
            [nitrox.analyser.doc.link
             namespaces
             numbers
             references
             tags]))

(ns/import nitrox.analyser.doc.link.namespaces [link-namespaces]
           nitrox.analyser.doc.link.numbers [link-numbers]
           nitrox.analyser.doc.link.references [link-references]
           nitrox.analyser.doc.link.tags [link-tags])
