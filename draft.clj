
(ns adjective-me-web.routes.user
  (:use compojure.core)
  (:require [adjective-me-web.layout :as layout]))

(defn assign-adjective [user-id adjective]
  (str "user:" user-id " as:" adjective))

(defn remove-adjective [user-id adjective]
  (str "user:" user-id " not as:" adjective))

(defn user-page []
  (layout/render "user.html"))

(inter)

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
