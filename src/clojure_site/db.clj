(ns clojure-site.db
  (:require

    ; непосредственно Monger
    [monger.core :as mg]
    [monger.collection :as m]
    [monger.operators :refer :all])

    ; Импортируем методы из Java библиотек
    (:import org.bson.types.ObjectId)
  )

; создадим переменную соединения с БД
(defonce db
         (let [uri "mongodb://127.0.0.1:27017/papamail"
               {:keys [db]} (mg/connect-via-uri uri)]
           db))

(defn get-mailings
  "Получение всех рассылок"
  []
  (m/find-maps db "mailing"))

(defn get-bases []
  "Получение всех баз"
  []
  (m/find-maps db "base"))

(defn create-base [base]
  (let [object-id (ObjectId.)]
    (m/insert db "base" (assoc base :_id object-id))))

(defn get-base [id]
  (let [id (ObjectId. id)]
    (m/find-map-by-id db "base" id)))

(defn get-templates []
  "Получение всех шаблонов"
  []
  (m/find-maps db "templates"))

(defn create-email [email]
  (let [object-id (ObjectId.)]
    (m/insert db "email" (assoc email :_id object-id))))

(defn get-emails [baseId]
  (m/find-maps db "email" {:base baseId}))

(defn create-mailing [mailing]
  (let [object-id (ObjectId.)]
    (m/insert db "mailing" (assoc mailing :_id object-id))))

(defn remove-mailing [id]
  (println id)
  (let [id (ObjectId. id)]
    (m/remove-by-id db "mailing" id)))
