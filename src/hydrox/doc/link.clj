(ns hydrox.doc.link
  (:require [hara.namespace.import :as ns]
            [hydrox.doc.link
             anchors
             namespaces
             numbers
             references
             stencil
             tags]))

(ns/import hydrox.doc.link.anchors [link-anchors link-anchors-lu]
           hydrox.doc.link.namespaces [link-namespaces]
           hydrox.doc.link.numbers [link-numbers]
           hydrox.doc.link.references [link-references]
           hydrox.doc.link.stencil [link-stencil]
           hydrox.doc.link.tags [link-tags])
