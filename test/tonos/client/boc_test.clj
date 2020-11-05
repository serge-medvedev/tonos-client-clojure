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
      (is (-> (boc/parse-transaction *context* params)
              doall
              last
              :params-json
              :parsed
              :balance_delta
              (= "0x9d350e60"))))))

(deftest parse-block-test
  (testing "block parsing to get seq_no"
    (let [params {:boc (-> tt/test-data :boc :block)}]
      (is (-> (boc/parse-block *context* params)
              doall
              last
              :params-json
              :parsed
              :seq_no
              (= 4810902))))))

(deftest parse-account-test
  (testing "account parsing"
    (let [params {:boc (-> tt/test-data :boc :account)}]
      (is (-> (boc/parse-account *context* params)
              doall
              last
              :params-json
              :parsed
              :id
              (= "0:7866e5e4edc40639331140807d2a2dc7d4bc53005bb34d71428cdd250c91b404"))))))


(deftest parse-message-test
  (testing "message parsing"
    (let [params {:boc (-> tt/test-data :boc :message)}]
      (is (-> (boc/parse-message *context* params)
              doall
              last
              :params-json
              :parsed
              :value
              (= "0x13c9e2af662000"))))))

(deftest get-blockchain-config-test
  (testing "getting blockchain config"
    (let [params {:block_boc (-> tt/test-data :boc :blockchain)}]
      (is (-> (boc/get-blockchain-config *context* params)
              doall
              last
              :params-json
              :config_boc
              string?)))))
