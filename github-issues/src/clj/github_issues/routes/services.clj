(ns github-issues.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [github-issues.search :refer [search-issues]]
            [github-issues.formatter :refer [format-issues]]
            [schema.core :as s]))

(def slack-access-token (get (System/getenv) "SLACK_ACCESS_TOKEN"))

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  (context "/api" []
    :tags ["GitHub Issues"]

    ; would be great to authorize through the middleware
    (GET "/herokuapps"
      {params :params}
      (if (= (:token params) slack-access-token)
        (ok (->
              (search-issues (dissoc params :token))
              format-issues))
        (forbidden {})))))
