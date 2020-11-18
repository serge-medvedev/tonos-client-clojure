(ns example.core
  (:require [tonos.client.core :as core]
            [tonos.client.client :as client])
  (:gen-class))
(defn -main
  []
  (let [config "{\"network\":{\"server_address\":\"https://net.ton.dev\"}}"
        context (core/create-context config)]
    (println (client/version! context))
    (core/destroy-context context)))

