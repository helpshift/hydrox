(ns hydrox.core.patch
  (:require [clojure.tools.reader.edn :as edn]
            [clojure.tools.reader.impl.commons :as common]
            [clojure.tools.reader.impl.utils :as utils]
            [clojure.tools.reader.reader-types :as reader]
            [clojure.tools.reader.impl.errors :as read-err]))

(defn read-keyword
  [reader initch opts]
  (let [ch (reader/read-char reader)]
    (if-not (utils/whitespace? ch)
      (let [token (#'edn/read-token reader ch)]
        (keyword token))
      (read-err/reader-error reader "Invalid token: :"))))

(defn patch-read-keyword []
  (alter-var-root #'clojure.tools.reader.edn/read-keyword
                  (fn [_]
                    read-keyword)))
