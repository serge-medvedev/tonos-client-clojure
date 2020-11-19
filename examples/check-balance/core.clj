(ns example.core
  (:require [tonos.client.core :as core]
            [tonos.client.net :as net])
  (:gen-class))

(def ^:dynamic *context* nil)
(def ^:const config "{\"network\":{\"server_address\":\"https://net.ton.dev\"}}")
(def ^:const coffee-shop "0:81236e4b0298f55b1d4d67d0f508cffa21466f42f646a829ff68ea4562f832bc")

(defn parse-hex
  [x]
  (-> (re-find #"^0x([0-9a-zA-Z]+)$" x)
      last
      (Long/parseLong 16)))

(defn get-balance
  [account]
  (let [params {:collection "accounts"
                :filter {:id {:eq account}}
                :result "balance"}]
    (-> (net/query-collection! *context* params)
        :result
        first
        :balance
        parse-hex)))

(defn -main
  []
  (binding [*context* (core/create-context config)]
    (println "Balance is" (get-balance coffee-shop) "nanotons")
    (core/destroy-context *context*)))

