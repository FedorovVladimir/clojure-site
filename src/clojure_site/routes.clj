(ns clojure-site.routes
  (:require

    ; работа с маршрутами
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :as route]

    ; контроллеры запросов
    ;[clojure-site.controllers :as c]

    ; отображение страниц
    [clojure-site.views :as v]

    ; функции взаимодействия с БД
    [clojure-site.db :as db]))

; объявляем маршруты
(defroutes mail-routes

           ; главная страница приложения
           (GET "/" []
             (let [mailings (db/get-mailings)]
               (v/index mailings)))

           ; страница баз
           (GET "/bases" []
             (let [bases (db/get-bases)]
               (v/bases bases)))

           ; страница шаблонов
           (GET "/templates" []
             (let [templates (db/get-templates)]
               (v/templates templates)))

           ; ошибка 404
           (route/not-found "Ничего не найдено"))