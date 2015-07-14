(ns hydrox.graph-test
  (:use midje.sweet)
  (:import [org.graphstream.graph Graph]
           [org.graphstream.graph.implementations SingleGraph]
           [org.graphstream.ui.swingViewer ViewPanel]))


(comment
  
  (def gph (SingleGraph. "Demo"))

  (.addNode gph "A")
  (.addNode gph "B")
  (.addNode gph "C")

  (.addEdge gph "AB" "A" "B")
  (.addEdge gph "BC" "B" "C")
  (.addEdge gph "CA" "C" "A")
  
  )



