(ns github-issues.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[github-issues started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[github-issues has shutdown successfully]=-"))
   :middleware identity})
