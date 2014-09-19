(ns correios-notifier.notifier
  (:require [postal.core :refer [send-message]]
            [correios-notifier :refer [uri->file copy-file]]))

(def correios-url "http://websro.correios.com.br/sro_bin/txect01$.QueryList?P_LINGUA=001&P_TIPO=001&P_COD_UNI=LN798157392US")
(def status-file-name "correios-status.html")
(def new-file-name "correios-status-new.html")
(def from-email "my@gmail.com")
(def from-email-passowrd "XXX")

(defn notify-update []
  (send-message {:host "smtp.gmail.com"
                  :user from-email
                  :pass from-email-passowrd
                  :ssl :blablabla}
                {:from from-email
                  :to ["to@gmail.com"]
                  :subject "Update no site dos Correios!!!"
                  :body "se liga: http://websro.correios.com.br/sro_bin/txect01$.QueryList?P_LINGUA=001&P_TIPO=001&P_COD_UNI=LN798157392US"}))

(defn update-and-notify []
  (copy-file new-file-name status-file-name)
  (notify-update))

(defn check-status []
  (uri->file correios-url new-file-name)
  (if (not= (slurp status-file-name) (slurp new-file-name))
    (update-and-notify)))

