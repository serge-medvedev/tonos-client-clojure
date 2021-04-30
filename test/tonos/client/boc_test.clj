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

(deftest encode-boc-test
  (letfn [(write-b         [value]      {:type "Integer" :size 1 :value value})
          (write-u128      [value]      {:type "Integer" :size 128 :value (str value)})
          (write-i         [value size] {:type "Integer" :size size :value value})
          (write-i8        [value]      (write-i value 8))
          (write-u8        [value]      (write-i value 8))
          (write-bitstring [value]      {:type "BitString" :value value})
          (write-cell      [write]      {:type "Cell" :builder write})]
    (let [expected-boc "te6ccgEBAgEAKQABL7/f4EAAAAAAAAAAAG2m0us0F8ViiEjLZAEAF7OJx0AnACRgJH/bsA=="]
      (testing "creation of a boc out of parts"
        (let [params {:builder [(write-b 1)
                                (write-b 0)
                                (write-u8 255)
                                (write-i8 127)
                                (write-i8 -127)
                                (write-u128 123456789123456789)
                                (write-bitstring "8A_")
                                (write-bitstring "x{8A0_}")
                                (write-bitstring "123")
                                (write-bitstring "x2d9_")
                                (write-bitstring "80_")
                                (write-cell [(write-bitstring "n101100111000")
                                             (write-bitstring "N100111000")
                                             (write-i -1 3)
                                             (write-i 2 3)
                                             (write-i 312 16)
                                             (write-i "0x123" 16)
                                             (write-i "0x123" 16)
                                             (write-i "-0x123" 16)])]}]
          (is (-> (boc/encode-boc! *context* params)
                  :boc
                  (= expected-boc)))))
      (testing "creation of a boc out of parts (using a cell boc)"
        (let [params {:builder [(write-b 1)
                                (write-b 0)
                                (write-u8 255)
                                (write-i8 127)
                                (write-i8 -127)
                                (write-u128 123456789123456789)
                                (write-bitstring "8A_")
                                (write-bitstring "x{8A0_}")
                                (write-bitstring "123")
                                (write-bitstring "x2d9_")
                                (write-bitstring "80_")
                                {:type "CellBoc" :boc "te6ccgEBAQEADgAAF7OJx0AnACRgJH/bsA=="}]}]
          (is (-> (boc/encode-boc! *context* params)
                  :boc
                  (= expected-boc))))))))
