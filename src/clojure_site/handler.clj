(ns clojure-site.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" []
    "<title>Clojure</title>
    <h1>Hello, I am web-site on Clojure!!!<h1>")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
