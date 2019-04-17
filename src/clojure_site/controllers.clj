(ns clojure-site.controllers
  (:require

    ; функция редиректа
    [ring.util.response :refer [redirect]]

    ; функция для взаимодействия с БД
    [clojure-site.db :as db]))

(defn delete
  "Контроллер удаления письма"
  [id]
  (do
    (db/remove-mail id)
    (redirect "/")))

(defn edit
  "Контроллер редактирования письма"
  [request]

  ; получаем данные из формы
  (let [mail-id (get-in request [:form-params "id"])
        mail {:title (get-in request [:form-params "subject"])
              :text  (get-in request [:form-params "text"])}]

    ; проверяем данные
    (if (and (not-empty (:title mail))
             (not-empty (:text mail)))
      ; все хорошо
      (do
        (db/update-mail mail-id mail)
        (redirect "/"))
      ; ошибка
      "Проверьте правильность введенных данных")))

(defn create
  "Контролер создания письма"
  [request]

  ; получаем данные из формы
  ; (ассоциативный массив)
  (let [mail {:title (get-in request [:form-params "title"])
              :text (get-in request [:form-params "text"])}]

    ; Проверим данные
    (if (and (not-empty (:title mail))
             (not-empty (:text mail)))

      ; все хорошо
      (do
        (db/create-mail mail)
        (redirect "/"))

      ; ошибка
      "Проверьте правильность введенных данных")))
