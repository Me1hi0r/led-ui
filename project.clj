(defproject led-ui "0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [com.h2database/h2 "1.4.200"]
                 [ring/ring-jetty-adapter "1.8.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler led-ui.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}}
  :main led-ui.handler)
