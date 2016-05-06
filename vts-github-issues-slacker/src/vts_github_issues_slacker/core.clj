(ns vts-github-issues-slacker.core
  (:require [tentacles.search :as gh-search]))

(defn assets-qa-issues
  []
  (gh-search/search-issues
    ""
    {:repo "viewthespace/viewthespace" :label "qa" :state "open" :team "viewthespace/big-assets"}
    {:access_token "3e6c92df50123349c404342c810d57774ddd6041"}))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
