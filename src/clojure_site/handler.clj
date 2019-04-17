(ns clojure-site.handler
  (:require [compojure.core :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def app
  (wrap-defaults clojure-site.routes site-defaults))
