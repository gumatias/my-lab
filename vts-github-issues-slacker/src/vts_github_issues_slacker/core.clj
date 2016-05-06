(ns vts-github-issues-slacker.core
  (:require [tentacles.search :as gh-search]))

(defn assets-qa-issues
  []
  (gh-search/search-issues
    ""
    {:repo "viewthespace/viewthespace" :label "qa" :state "open" :team "viewthespace/big-assets"}
    {:access_token "ee282ebac13b3fcdc84670ca4683c4f3106e667d"}))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
