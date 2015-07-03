(ns nitrox.analyser.doc.link
  (:require [hara.namespace.import :as ns]
            [nitrox.analyser.doc.link
             anchors
             namespaces
             numbers
             references
             stencil
             tags]))

(ns/import nitrox.analyser.doc.link.anchors [link-anchors link-anchors-lu]
           nitrox.analyser.doc.link.namespaces [link-namespaces]
           nitrox.analyser.doc.link.numbers [link-numbers]
           nitrox.analyser.doc.link.references [link-references]
           nitrox.analyser.doc.link.stencil [link-stencil]
           nitrox.analyser.doc.link.tags [link-tags])
