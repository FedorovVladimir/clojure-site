(ns clojure-site.db
  (:require

    ; непосредственно Monger
    monger.joda-time ; для добавления времени и даты
    [monger.core :as mg]
    [monger.collection :as m]
    [monger.operators :refer :all]

    ; время и дата
    [joda-time :as t])

    ; Импортируем методы из Java библиотек
    (:import org.bson.types.ObjectId
             org.joda.time.DateTimeZone))

; во избежание ошибок нужно указать часовой пояс
(DateTimeZone/setDefault DateTimeZone/UTC)

; создадим переменную соединения с БД
(defonce db
         (let [uri "mongodb://127.0.0.1:27017"
               {:keys [db]} (mg/connect-via-uri uri)]
           db))

; приватная функция создания штампа даты и времени
(defn- date-time
  "Текущие дата и время"
  []
  (t/date-time))

(defn remove-mail
  "Удалить письмо по ее id"
  [id]
  ; переформатируем строку в id
  (let [id (ObjectId. id)]
    (m/remove-by-id db "mail" id)))

(defn update-mail
  "Обновить письмо по ее id"
  [id mail]
  ; Переформатируем строку в id
  (let [id (ObjectId. id)]
    (m/update db "mail" {:id id}
              ; Обновим помимо документа
              ; дату его создания
              {$set (assoc mail
                      :created (date-time))})))

(defn get-mail
  "Получить письмо по ее id"
  [id]
  (let [id (ObjectId. id)]
    ; Эта функция вернет hash-map найденного документа
    (m/find-map-by-id db "mail" id)))

(defn get-mails
  "Получить все письма"
  []
  ; Find-maps возвращает все документы
  ; из коллеции в виде hash-map
  (m/find-maps db "mail"))

(defn create-mail
  "Создать письмо в БД"
  ; Наше письмо принимается от котролера
  ; и имеет тип hash-map c видом:
  ; {:subject "Заголовок" :text "Содержание"}
  [mail]
  ; Monger может сам создать id
  ; но разработчиками настоятельно рекомендуется
  ; добавить это поле самостоятельно
  (let [object-id (ObjectId.)]
    ; Нам остается просто передать hash-map
    ; функции создания документа, только
    ; добавим в него сгенерированный id
    ; и штамп даты и времени создания
    (m/insert db "mail" (assoc mail
                           :_id object-id
                           :created (date-time)))))

