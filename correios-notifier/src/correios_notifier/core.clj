(ns correios-notifier.core
  (:require [correios-notifier.notifier :refer [check-status]])
  (:gen-class))

(defn -main [& args]
  (check-status))

