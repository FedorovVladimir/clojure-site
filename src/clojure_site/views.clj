(ns clojure-site.views
  (:require

    ; "Шаблонизатор"
    [selmer.parser :as parser]

    ; для HTTP заголовков
    [ring.util.response :refer [content-type response]]

    ; для CSRF защиты
    [ring.util.anti-forgery :refer [anti-forgery-field]]))

; подскажем Selmer где искать наши шаблоны
(parser/set-resource-path! (clojure.java.io/resource "templates"))

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

(defn index
  "Главная страница приложения. Список писем"
  [mails]
  (render "index.html"
          {:mails (if (not-empty mails)
                    mails false)}))

(defn bases [bases]
  (render "bases.html"
          {:bases (if (not-empty bases)
                    bases false)}))

(defn templates [templates]
  (render "templates.html"
          {:templates (if (not-empty templates)
                        templates false)}))