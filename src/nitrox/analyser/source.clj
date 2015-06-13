
(defn gather-source-file
  "collects the source code for a particular file
 
   (gather-source-file \"src/leiningen/midje_doc/common/gather.clj\")
   => (just-in {'leiningen.midje-doc.common.gather
                    {'gather-source-file {:source string?}
                     'gather-fact-body   {:source string?}
                     'gather-fact        {:source string?}
                     'gather-test-file   {:source string?}
                     'gather             {:source string?}}})"
  {:added "0.2"}
  [file]
  (let [zloc (source/of-file file)
        nsp  (->  (query/$ zloc [(ns | _ & _)] {:walk :top})
                  first)
        fns  (->> (query/$ zloc [(#{defn defmulti} | _ ^:%?- string? ^:%?- map? & _)]
                           {:return :zipper :walk :top})
                  (map (juxt source/sexpr
                             (comp #(hash-map :source %)
                                   source/string
                                   source/up)))
                  (into {}))]
    {nsp fns}))
