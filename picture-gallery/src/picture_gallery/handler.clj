(ns picture-gallery.handler
  (:require [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [picture-gallery.routes.home :refer [home-routes]]
            [noir.util.middleware :as noir-middleware]
            [picture-gallery.routes.auth :refer [auth-routes]]
            [picture-gallery.routes.upload :refer [upload-routes]]
            [noir.session :as session]
            [picture-gallery.routes.gallery :refer [gallery-routes]]
            [taoensso.timbre :as timbre]))

(defn info-appender [{:keys [level message]}]
  (println "level:" level "message:" message))

(defn init []
  (timbre/set-config!
    [:appenders :info-appender]
    {:min-level :info
     :enabled? true
     :async? false
     :max-message-per-msecs 100
     :fn info-appender})
  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "/var/log/picture-gallery.log" :max-size (* 512 1024) :backlog 10})
  (timbre/info "picture-gallery started successfuly"))

(defn destroy []
  (timbre/info "picture-gallery is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn user-page [_]
  (session/get :user))

(def app
  (noir-middleware/app-handler 
    [auth-routes 
     home-routes 
     upload-routes 
     gallery-routes
     app-routes]
     :access-rules [user-page]))


