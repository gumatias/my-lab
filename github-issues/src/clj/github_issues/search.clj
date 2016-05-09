(ns github-issues.search
  (:require [tentacles.search :as github]))

(def ^:private github-access-token (get (System/getenv) "GITHUB_ACCESS_TOKEN"))

(defn- filter-data
  [issues]
  (map #(select-keys %1 [:title :number :html_url]) (:items issues)))

(defn search-issues
  [params]
  (->
    (github/search-issues nil params {:access_token github-access-token})
    filter-data))

