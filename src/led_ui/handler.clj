(ns led-ui.handler
  (:require
   [led-ui.views :as views]
   [compojure.core :as cc]
   [compojure.handler :as handler]
   [compojure.route :as route]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
   (:gen-class))


(cc/defroutes app-routes
  (cc/GET "/"
    []
    (views/home-page))
  (cc/GET "/add-color"
    []
    (views/add-color-page))
  (cc/POST "/add-color"
    {params :params}
    (views/color-results-page params))
  (cc/GET "/color/:color-id"
    [color-id]
    (views/color-page color-id))
  (cc/GET "/palette"
    []
    (views/palette-page))
  (route/resources "/assets/")
  (route/not-found "Not Found"))

(def app
  (handler/site #'app-routes))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port  port
                            :join? false})))
