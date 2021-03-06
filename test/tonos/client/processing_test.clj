(ns tonos.client.processing-test
  (:require
    [clojure.test :refer :all]
    [tonos.client.test-tools :as tt]
    [tonos.client.abi :as abi]
    [tonos.client.crypto :as crypto]
    [tonos.client.processing :as processing]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)
(def ^:dynamic *message-encode-params* nil)
(def ^:dynamic *encoded* nil)

(def events-abi {:type "Contract"
                 :value (-> tt/test-data :events :abi)})
(def events-tvc (-> tt/test-data :events :tvc))

(defn context-fixture
  [f]
  (binding [*context* (core/create-context tt/dev-net-config)]
    (let [keypair (crypto/generate-random-sign-keys! *context*)]
      (binding [*message-encode-params* {:abi events-abi
                                         :deploy_set {:tvc events-tvc}
                                         :call_set {:function_name "constructor"
                                                    :header {:pubkey (:public keypair)}}
                                         :signer {:type "Keys" :keys keypair}}]
        (binding [*encoded* (abi/encode-message! *context* *message-encode-params*)]
          (tt/fund-account *context* (:address *encoded*))
          (f)
          (core/destroy-context *context*))))))

(use-fixtures :each context-fixture)

(deftest ^:slow ^:paid wait-for-transaction-test
  (testing "messge sending and waiting for transaction"
    (let [params {:message (:message *encoded*)
                            :send_events true}
          shard-block-id (->> (processing/send-message *context* params)
                              (filter (fn [msg]
                                        (and (-> msg :params-json (contains? :shard_block_id))
                                             (-> msg :response-type (= 0)))))
                              first
                              :params-json
                              :shard_block_id)]
      (is (not (nil? shard-block-id)))
      (let [params {:abi events-abi
                    :message (:message *encoded*)
                    :shard_block_id shard-block-id
                    :send_events true}
            result (->> (processing/wait-for-transaction *context* params)
                        (reduce (fn [acc msg]
                                  (if (-> msg :params-json :transaction :status_name (= "finalized"))
                                      (assoc acc :finalized true)
                                      acc))
                                {:finalized false}))]
      (is (= result {:finalized true}))))))

(deftest ^:slow ^:paid process-message-test
  (testing "message processing"
    (let [params {:message_encode_params *message-encode-params*
                  :send_events true}
          result (->> (processing/process-message *context* params)
                      (reduce (fn [acc msg]
                                (cond
                                  (-> msg :params-json :type (= "WillSend")) (update acc :WillSend inc)
                                  (-> msg :params-json :type (= "DidSend")) (update acc :DidSend inc)
                                  (-> msg :params-json :transaction :status_name (= "finalized")) (assoc acc :finalized true)
                                  :else acc))
                              {:WillSend 0 :DidSend 0 :finalized false}))]
      (is (= result {:WillSend 1 :DidSend 1 :finalized true})))))

