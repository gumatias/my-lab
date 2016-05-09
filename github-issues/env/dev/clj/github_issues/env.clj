(ns github-issues.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [github-issues.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[github-issues started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[github-issues has shutdown successfully]=-"))
   :middleware wrap-dev})
