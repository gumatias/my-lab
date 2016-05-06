(ns vts-github-issues-slacker.core
  (:require [tentacles.search :refer [search-issues]]
            [clj-slack.chat :refer [post-message]]))

; GITHUB
(def github-access-token "")

(defn assets-qa-issues
  "Do not commit access token, or else GitHub will invalidate"
  []
  (search-issues
    ""
    {:repo "viewthespace/viewthespace" :label "qa" :state "open" :team "viewthespace/big-assets"}
    {:access_token github-access-token}))

; SLACK
(def slack-access-token "")
(def connection {:api-url "https://slack.com/api" :token slack-access-token})
(def gus-test-group-id "G0KTFML3U")

(defn post-qa-issues-on-assets-channel
  []
  (post-message connection gus-test-group-id "Testando galerinha do mal ^.^"))
