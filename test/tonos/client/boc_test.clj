(ns tonos.client.boc-test
  (:require
    [clojure.test :refer :all]
    [tonos.client.test-tools :as tt]
    [tonos.client.boc :as boc]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)

(defn- tf
  [f]
  (binding [*context* (core/create-context tt/main-net-config)]
    (f)
    (core/destroy-context *context*)))

(use-fixtures :once tf)

(deftest parse-transaction-test
  (testing "transaction parsing to get balance delta"
    (let [params {:boc (-> tt/test-data :boc :transaction)}]
      (is (-> (boc/parse-transaction! *context* params)
              :parsed
              :balance_delta
              (= "0x9d350e60"))))))

(deftest parse-block-test
  (testing "block parsing to get seq_no"
    (let [params {:boc (-> tt/test-data :boc :block)}]
      (is (-> (boc/parse-block! *context* params)
              :parsed
              :seq_no
              (= 4810902))))))

(deftest parse-account-test
  (testing "account parsing"
    (let [params {:boc (-> tt/test-data :boc :account)}]
      (is (-> (boc/parse-account! *context* params)
              :parsed
              :id
              (= "0:7866e5e4edc40639331140807d2a2dc7d4bc53005bb34d71428cdd250c91b404"))))))


(deftest parse-message-test
  (testing "message parsing"
    (let [params {:boc (-> tt/test-data :boc :message)}]
      (is (-> (boc/parse-message! *context* params)
              :parsed
              :value
              (= "0x13c9e2af662000"))))))

(deftest get-boc-hash-test
  (testing "getting the hash of a boc"
    (let [params {:boc "te6ccgEBAQEAWAAAq2n+AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAE/zMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzSsG8DgAAAAAjuOu9NAL7BxYpA" }]
      (is (-> (boc/get-boc-hash! *context* params)
              :hash
              (= "dfd47194f3058ee058bfbfad3ea40cbbd9ad17ca77cd0904d4d9f18a48c2fbca"))))))

(deftest parse-shardstate-test
  (testing "shard state parsing"
    (let [params {:boc (-> tt/test-data :boc :shardstate)
                  :workchain_id -1
                  :id "zerostate:-1"}
          result (-> (boc/parse-shardstate! *context* params)
                     :parsed)]
      (is (-> result :id (= (:id params))))
      (is (-> result :workchain_id (= (:workchain_id params))))
      (is (-> result :seq_no (= 0))))))

(deftest get-blockchain-config-test
  (testing "getting blockchain config"
    (let [params {:block_boc (-> tt/test-data :boc :blockchain)}]
      (is (-> (boc/get-blockchain-config! *context* params)
              :config_boc
              string?)))))

