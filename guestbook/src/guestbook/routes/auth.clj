(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer [form-to label text-field password-field submit-button]]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.validation :refer [rule errors? has-value? on-error]]
            [noir.util.crypt :as crypt]))

(defn format-error [[error]]
  [:p.error error])

(defn control [field name text]
  (list (on-error name format-error)
        (label name text)
        (field name)
        [:br]))

(defn registration-page []
  (layout/common
    (form-to [:post "/register"]
             (control text-field :id "screen name")
             (control password-field :pass "password")
             (control password-field :pass1 "retype password")
             (submit-button "Create Account"))))

(defn login-page []
  (layout/common
    (form-to [:post "/login"]
      (control text-field :id "screen name")
      (control password-field :pass "Password")
      (submit-button "login"))))

(defroutes auth-routes 
  (GET "/register" [_] (registration-page))
  (POST "/register" [id pass pass1]
        (if (= pass pass1)
          (redirect "/")
          (registration-page)))
  (GET "/login" [] (login-page))
  (POST "/login" [id pass]
        (session/put! :user id)
        (redirect "/"))
  (GET "/logout" [] 
       (layout/common 
         (form-to [:post "/logout"]
            (submit-button "logout"))))
  (POST "/logout" []
        (session/clear!)
        (redirect "/")))

(defn handle-login [id pass]
  (rule (has-value? id)
        [:id "screen name is required"])
  (rule (= id "foo")
        [:id "unknown user"])
  (rule (has-value? pass)
        [:pass "Password required"])
  (rule (= pass "bar")
        [:pass "Invalid password"])
  (if (errors? :id :pass)
    (login-page)
    (do 
      (session/put! :user id)
      (redirect "/"))))
