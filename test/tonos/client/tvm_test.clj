(ns tonos.client.tvm-test
  (:require
    [clojure.test :refer :all]
    [clojure.string :as string]
    [tonos.client.test-tools :as tt]
    [tonos.client.abi :as abi]
    [tonos.client.crypto :as crypto]
    [tonos.client.tvm :as tvm]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)
(def ^:dynamic *elector-encoded* nil)

(defn- tf
  [f]
  (binding [*context* (core/create-context tt/dev-net-config)]
    (let [params {:state_init {:type "StateInit"
                               :code (-> tt/test-data :elector :code)
                               :data (-> tt/test-data :elector :data)}}]
      (binding [*elector-encoded* (-> (abi/encode-account *context* params)
                                      doall
                                      last
                                      :params-json)]
        (f)
        (core/destroy-context *context*)))))

(use-fixtures :once tf)

(deftest ^:slow ^:paid heavy-run-*-methods-test
  (testing "making and checking subsription"
    (let [subscription-abi {:type "Serialized"
                            :value (-> tt/test-data :subscription :abi)}
          signer {:type "Keys"
                  :keys (-> (crypto/generate-random-sign-keys *context*)
                            doall
                            last
                            :params-json)}
          encode-message-params {:abi subscription-abi
                                 :deploy_set {:tvc (-> tt/test-data :subscription :tvc)}
                                 :call_set {:function_name "constructor"
                                            :input {:wallet "0:2222222222222222222222222222222222222222222222222222222222222222"}}
                                 :signer signer}
          encoded (-> (abi/encode-message *context* encode-message-params)
                      doall
                      last
                      :params-json)]

      (tt/fund-account *context* (:address encoded))

      (let [account (-> (tt/fetch-account *context* (:address encoded))
                        :boc)
            subscribe-params {:subscriptionId "0x1111111111111111111111111111111111111111111111111111111111111111"
                              :pubkey "0x2222222222222222222222222222222222222222222222222222222222222222"
                              :to "0:3333333333333333333333333333333333333333333333333333333333333333"
                              :value "0x123"
                              :period "0x456"}
            encode-message-params {:abi subscription-abi
                                   :address (:address encoded)
                                   :deploy_set {:tvc (-> tt/test-data :subscription :tvc)}
                                   :call_set {:function_name "subscribe"
                                              :input subscribe-params}
                                   :signer signer}
            encoded (-> (abi/encode-message *context* encode-message-params)
                        doall
                        last
                        :params-json)
            run-executor-params {:abi subscription-abi
                                 :message (:message encoded)
                                 :account {:type "Account"
                                           :boc account}}
            result (-> (tvm/run-executor *context* run-executor-params)
                       doall
                       last
                       :params-json)]

        (is (-> result
                :transaction
                :in_msg
                (= (:message_id encoded))))
        (is (-> result
                :fees
                :total_account_fees
                (> 0)))

        (let [account (:account result)
              encode-message-params {:abi subscription-abi
                                     :address (:address encoded)
                                     :call_set {:function_name "getSubscription"
                                                :input {:subscriptionId (:subscriptionId subscribe-params)}}
                                     :signer signer}
              encoded (-> (abi/encode-message *context* encode-message-params)
                          doall
                          last
                          :params-json)
              run-tvm-params {:abi subscription-abi
                              :account account
                              :message (:message encoded)}
              result (-> (tvm/run-tvm *context* run-tvm-params)
                         doall
                         last
                         :params-json)]
          (is (-> result
                  :decoded
                  :output
                  :value0
                  :pubkey
                  (= (:pubkey subscribe-params)))))))))

(defn- count-participants
  [[x xs]]
  (if (nil? xs) 0 (-> xs count-participants (+ 1))))

(deftest run-get-test
  (testing "getting the list of participants of the elector"
    (let [params {:account (:account *elector-encoded*)
                  :function_name "participant_list"}]
      (is (-> (tvm/run-get *context* params)
              doall
              last
              :params-json
              :output
              first
              count-participants
              (= 108)))))
  (testing "getting the list of participants of the elector"
    (let [params {:account (:account *elector-encoded*)
                  :function_name "compute_returned_stake"
                  :input (format "0x%s"
                                 (-> tt/test-data
                                     :elector
                                     :address
                                     (string/split #":")
                                     last))}]
      (is (-> (tvm/run-get *context* params)
              doall
              last
              :params-json
              :output
              first
              (= "0")))))
  (testing "getting the past elections info from the elector"
    (let [params {:account (:account *elector-encoded*)
                  :function_name "past_elections"}]
      (is (-> (tvm/run-get *context* params)
              doall
              last
              (get-in [:params-json :output 0 0 0])
              (= "1588268660"))))))

