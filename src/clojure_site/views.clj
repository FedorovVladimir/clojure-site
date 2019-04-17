(ns clojure-site.views
  (:require

    ; "Шаблонизатор"
    [selmer.parser :as parser]
    [selmer.filters :as filters]

    ; время и дата
    [joda-time :as t]

    ; для HTTP заголовков
    [ring.util.response :refer [content-type response]]

    ; для CSRF защиты
    [ring.util.anti-forgery :refer [anti-forgery-field]]))

; подскажем Selmer где искать наши шаблоны
(parser/set-resource-path! (clojure.java.io/resource "templates"))

; чтобы привести дату в человеко-понятный формат
(defn format-date-and-time
  "Отформатировать дату и время"
  [date]
  (let [formatter (t/formatter "yyyy-MM-dd в H:m:s" :date-time)]
    (when date
      (t/print formatter date))))

; добавим фильтр для использования в шаблоне
(filters/add-filter! :format-datetime
                     (fn [content]
                       [:safe (format-date-and-time content)]))

; Добавим тэг с полем для форм в нем будет находится
; автоматически созданное поле с anti-forgery ключом
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))

(defn render [template & [params]]
  "Эта функция будет отображать наши html шаблоны
  и передавать в них данные"
  (-> template
      (parser/render-file
        ; Добавим к получаемым данным постоянные
        ; значения которые хотели бы получать
        ; на любой странице
        (assoc params
          :title "Менеджер писем"
          :page (str template)))
      ; Из всего этого сделаем HTTP ответ
      response
      (content-type "text/html; charset=utf-8")))

(defn mail
  "Страница просмотра письма"
  [mail]
  (render "mail.html"
          ; передаем данные в шаблон
          {:mail mail}))

(defn edit
  "Страница редактирования письма"
  [mail]
  (render "edit.html"
          ; передаем данные в шаблон
          {:mail mail}))

(defn create
  "Страница создания письма"
  []
  (render "create.html"))

(defn index
  "Главная страница приложения. Список писем"
  [mails]
  (render "index.html"
          ; Передаем данные в шаблон
          ; Если mails пуст вернуть false
          {:mails (if (not-empty mails)
                    mails false)}))