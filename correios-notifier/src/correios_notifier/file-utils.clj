(ns correios-notifier.file-utils
  (:require [clojure.java.io :as io]))

(defn uri->file [uri file]
  (with-open [in (clojure.java.io/input-stream uri)
              out (clojure.java.io/output-stream file)]
  (clojure.java.io/copy in out)))

(defn copy-file [source-path dest-path]
    (io/copy (io/file source-path) (io/file dest-path)))

