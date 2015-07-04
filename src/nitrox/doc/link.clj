(ns nitrox.doc.link
  (:require [hara.namespace.import :as ns]
            [nitrox.doc.link
             anchors
             namespaces
             numbers
             references
             stencil
             tags]))

(ns/import nitrox.doc.link.anchors [link-anchors link-anchors-lu]
           nitrox.doc.link.namespaces [link-namespaces]
           nitrox.doc.link.numbers [link-numbers]
           nitrox.doc.link.references [link-references]
           nitrox.doc.link.stencil [link-stencil]
           nitrox.doc.link.tags [link-tags])
