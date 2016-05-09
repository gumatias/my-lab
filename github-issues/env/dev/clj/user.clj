(ns user
  (:require [mount.core :as mount]
            github-issues.core))

(defn start []
  (mount/start-without #'github-issues.core/repl-server))

(defn stop []
  (mount/stop-except #'github-issues.core/repl-server))

(defn restart []
  (stop)
  (start))


