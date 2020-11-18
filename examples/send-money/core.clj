(ns example.core
  (:require [tonos.client.core :as core]
            [tonos.client.processing :as processing])
  (:gen-class))

(def ^:dynamic *context* nil)
(def ^:const config "{\"network\":{\"server_address\":\"https://net.ton.dev\"}}")
(def ^:const wallet-abi "{\"ABI version\":2,\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]}],\"data\":[],\"events\":[]}\r\n")
(def my-wallet {:addr "..."
                :keys {:public "..."
                       :secret "..."}})
(def ^:const coffee-shop "0:81236e4b0298f55b1d4d67d0f508cffa21466f42f646a829ff68ea4562f832bc")

(defn send-money
  [from to nanotons keypair]
  (let [now (System/currentTimeMillis)
        params {:message_encode_params {:abi {:type "Json"
                                              :value wallet-abi}
                                        :address from
                                        :call_set {:function_name "sendTransaction"
                                                   :header {:pubkey (:public keypair)
                                                            :time now
                                                            :expire (-> now (quot 1000) (+ 10))}
                                                   :input {:dest to
                                                           :value nanotons
                                                           :bounce false
                                                           :flags 0
                                                           :payload ""}}
                                        :signer {:type "Keys"
                                                 :keys keypair}}
                :send_events false}]
    (doseq [msg (processing/process-message *context* params)]
      (when (-> msg :response-type (= 1))
        (println "ERROR:" (-> msg :params-json :message)))
      (when (-> msg :params-json :transaction :status_name (= "finalized"))
        (println "Money sent")))))

(defn -main
  []
  (binding [*context* (core/create-context config)]
    (send-money (:addr my-wallet)
                coffee-shop
                1000000000
                (:keys my-wallet))
    (core/destroy-context *context*)))

