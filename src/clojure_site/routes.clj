(ns clojure-site.routes
  (:require

    ; работа с маршрутами
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :as route]

    ; контроллеры запросов
    [clojure-site.controllers :as c]

    ; отображение страниц
    [clojure-site.views :as v]

    ; функции взаимодействия с БД
    [clojure-site.db :as db]))

; объявляем маршруты
(defroutes mail-routes

           ; страница просмотра письма
           (GET "/mail/:id" [id]
             (let [mail (db/get-mail id)]
               (v/mail mail)))

           ; контроллер удаления письма по id
           (GET "/delete/:id" [id]
             (c/delete id))

           ; обработчик редактирования письма
           (POST "/edit/:id" request
             (-> c/edit))

           ; страница редактирования письма
           (GET "/edit/:id" [id]
             (let [mail (db/get-mail id)]
               (v/edit mail)))

           ; обработчик добавления письма
           (POST "/create"
             (-> c/create))

           ; страница добавления письма
           (GET "/create" []
             (v/create))

           ; главная страница приложения
           (GET "/" []
             (let [mails (db/get-mails)]
               (v/index mails)))

           ; ошибка 404
           (route/not-found "Ничего не найдено"))