(ns led-ui.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./db/rgb-colors"})

(defn hex2ints
  [hex]
  (let [hex-seq (clojure.string/split hex #"")
        red (take 2 (drop 1 hex-seq))
        green (take 2 (drop 3 hex-seq))
        blue (take 2 (drop 5 hex-seq))]
    (map #(-> (conj % "0x") (clojure.string/join) (read-string)) [red green blue])))

(defn add-color-to-db
  [color]
  (let
   [[r g b] (hex2ints color)
    results (jdbc/insert! db-spec :colors {:red r :green g :blue b})]
    (assert (= (count results) 1))
    (first (vals (first results)))))

(defn get-rgb
  [loc-id]
  (let [results (jdbc/query db-spec
                            ["select red, green, blue from colors where id = ?" loc-id])]
    (assert (= (count results) 1))
    (first results)))

(defn get-all-colors
  []
  (jdbc/query db-spec "select id, red, green, blue from colors"))

(comment
  (require '[clojure.java.jdbc :as jdbc])
  (jdbc/with-db-connection [conn {:dbtype "h2" :dbname "./rgb-colors"}]
    (jdbc/db-do-commands conn
                         (jdbc/create-table-ddl :colors
                                                [[:id "bigint primary key auto_increment"]
                                                 [:red "integer"]
                                                 [:green "integer"]
                                                 [:blue "integer"]]))))