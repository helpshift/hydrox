(ns nitrox.repository
  (:require [gita.core :as git]
            [gita.api.repository :as repository]))

(comment

  (git/git :log :help)

  (git/git :status :help)

  (git/git :log)

  (def repo (git/repository))

  (def cid (.resolve repo "3ce"))

  (repository/list-files)
  )
