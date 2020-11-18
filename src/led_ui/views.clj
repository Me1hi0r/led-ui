(ns led-ui.views
  (:require [led-ui.db :as db]
            [clojure.string :as str]
            [hiccup.page :as page]
            [ring.util.anti-forgery :as util]))

(defn js
  [path]
  [:script {:src path}])

(defn background
  [color]
  (format "background-color: rgb(%s, %s, %s);"
          (:red color)
          (:green color)
          (:blue color)))

(defn sum
  [x lyz]
  (+ x lyz))

(+ 1 1)
(defn gen-page-head
  [title]
  [:head
   [:title (str "Led: " title)]
   (page/include-css "assets/css/style.css")
   (js "assets/js/mqttws31.js")
   (js "assets/js/jscolor.js")])


(defn home-page
  []
  (page/html5
   (gen-page-head "Home")
   [:div.container
    [:div.text "This application can control the addressable LED strip. To do this, you need to select a color from the " 
               [:a {:href "/palette"} "palette"] ". It is also possible to " [:a {:href "/add-color"} "add new colors"] " to the db."]]))



(defn add-color-page
  []
  (page/html5
   (gen-page-head "Add a color")
   [:div.container
    [:div.text "You can select a new color"]
    [:form {:action "/add-color" :method "POST"}
     (util/anti-forgery-field)
     [:input {:type "text" :name "color" "data-jscolor" "hash: false;"}]
    [:button.shape.ripple {:type "submit"} "SAVE"]]]
   (js "assets/js/script.js")))

(defn color-results-page
  [{:keys [color]}]
  (let [id (db/add-color-to-db color)]
    (page/html5
     (gen-page-head "Added a color")
     [:div.container
      [:div.text "New color add to "
       [:a {:href "/palette"} "palete"]
       "."]
      [:div.palette
       [:div.color-box {:style (format "background-color: %s" color)}]]])))

(defn color-page
  [color-id]
  (let [{r :red g :green b :blue} (db/get-rgb color-id)]
    (page/html5
     (gen-page-head (str "Color " color-id))
     [:h1 "A single color"]
     [:p "id: " color-id]
     [:p "red: " r]
     [:p "green: " g]
     [:p "blue: " b])))



(defn palette-page
  []
  (let [all-colors (db/get-all-colors)]
    (page/html5
     (gen-page-head "Palette")
     [:div.container
      [:div.text "Choose color or "
       [:a {:href "/add-color"} "create another one"] "."]
      [:div.palette
       (for [color all-colors]
         [:div.color-box {:style (background color)}])]]
     (js "assets/js/script.js"))))

