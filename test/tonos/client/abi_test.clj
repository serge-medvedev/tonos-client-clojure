(ns tonos.client.abi-test
  (:require
    [clojure.test :refer :all]
    [tonos.client.test-tools :as tt]
    [tonos.client.abi :as abi]
    [tonos.client.boc :as boc]
    [tonos.client.crypto :as crypto]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)

(def events-abi {:type "Contract"
                 :value (-> tt/test-data :events :abi)})
(def keys-signer {:type "Keys"
                  :keys tt/keypair})
(def external-signer {:type "External"
                      :public_key (:public tt/keypair)})

(defn- tf
  [f]
  (binding [*context* (core/create-context tt/dev-net-config)]
    (f)
    (core/destroy-context *context*)))

(use-fixtures :once tf)

(deftest encode-account-test
  (testing "account data encoding"
    (let [params (boc/encode-state-init! *context* {:type "StateInit"
                                                    :code (-> tt/test-data :elector :code)
                                                    :data (-> tt/test-data :elector :data)})]
      (is (-> (abi/encode-account! *context* params)
              :id
              (= "1089829edf8ad38e474ce9e93123b3281e52c3faff0214293cbb5981ee7b3092"))))))

(deftest encode-message-test
  (testing "message encoding"
    (let [params {:abi events-abi
                  :deploy_set {:tvc (-> tt/test-data :events :tvc)}
                  :call_set {:function_name "constructor"
                             :header {:pubkey (:public tt/keypair)}}
                  :signer keys-signer}
          result (abi/encode-message! *context* params)]
      (is (-> result :address string?))
      (is (-> result :message string?)))))

(deftest encode-message-body-test
  (testing "message body encoding"
    (let [params {:abi events-abi
                  :call_set {:function_name "returnValue"
                             :header {:pubkey (:public tt/keypair)
                                      :time 1599458364291
                                      :expire 1599458404}
                             :input {:id 0}}
                  :is_internal false
                  :signer keys-signer}
          expected "te6ccgEBAgEAlgAB4eb6eSBDZAg2YZ4IJ5P+cReLJ2jL1KmQPkzEKKsLLaZRiYUzUBzHX7IgJ0ZqQUGt44+ckKJ1BLDWadBa7O7OQALE0xnkQqgvUQQ4LYjedUXrxPfmboEdkvKBuC6hsc2uAoAAAF0ZyXLg19VzGQVviwSgAQBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="]
      (is (-> (abi/encode-message-body! *context* params)
              :body
              (= expected))))))

(deftest attach-signature-test
  (testing "message signing"
    (let [encoded (tt/create-encoded-message *context* external-signer 1599458364291 1599458404)
          nacl-sign-detached-params {:unsigned (:message encoded)
                                     :secret (str (:secret tt/keypair) (:public tt/keypair))}
          signature (-> (crypto/nacl-sign-detached! *context* nacl-sign-detached-params)
                        :signature)
          attach-signature-params {:abi events-abi
                                   :public_key (:public tt/keypair)
                                   :message (:message encoded)
                                   :signature signature}
          expected "te6ccgECGAEAA6wAA0eIAGdkEejrY7lo2CR2+sfFyrbt7lCqqU8QkEW6xgcUNSFMEbAHAgEA4f5wn3Xd1yv5w4IsitWh5eKl3dCyks6u6bomccs8mfmQXMXLcoyAd2MBOKPmsToGFICTw68Iq3mzFFEaLdFpUwJE0xnkQqgvUQQ4LYjedUXrxPfmboEdkvKBuC6hsc2uAoAAAF0ZyXLg19VzGRotV8/gAQHAAwIDzyAGBAEB3gUAA9AgAEHYmmM8iFUF6iCHBbEbzqi9eJ78zdAjsl5QNwXUNjm1wFQCJv8A9KQgIsABkvSg4YrtU1gw9KEKCAEK9KQg9KEJAAACASANCwHI/38h7UTQINdJwgGOENP/0z/TANF/+GH4Zvhj+GKOGPQFcAGAQPQO8r3XC//4YnD4Y3D4Zn/4YeLTAAGOHYECANcYIPkBAdMAAZTT/wMBkwL4QuIg+GX5EPKoldMAAfJ64tM/AQwAao4e+EMhuSCfMCD4I4ED6KiCCBt3QKC53pL4Y+CANPI02NMfAfgjvPK50x8B8AH4R26S8jzeAgEgEw4CASAQDwC9uotV8/+EFujjXtRNAg10nCAY4Q0//TP9MA0X/4Yfhm+GP4Yo4Y9AVwAYBA9A7yvdcL//hicPhjcPhmf/hh4t74RvJzcfhm0fgA+ELIy//4Q88LP/hGzwsAye1Uf/hngCASASEQDluIAGtb8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFvfSDK6mjofSBv6PwikDdJGDhvfCFdeXAyfABkZP2CEGRnwoRnRoIEB9AAAAAAAAAAAAAAAAAAIGeLZMCAQH2AGHwhZGX//CHnhZ/8I2eFgGT2qj/8M8ADFuZPCot8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFva4b/yupo6Gn/7+j8AGRF7gAAAAAAAAAAAAAAAAhni2fA58jjyxi9EOeF/+S4/YAYfCFkZf/8IeeFn/wjZ4WAZPaqP/wzwAgFIFxQBCbi3xYJQFQH8+EFujhPtRNDT/9M/0wDRf/hh+Gb4Y/hi3tcN/5XU0dDT/9/R+ADIi9wAAAAAAAAAAAAAAAAQzxbPgc+Rx5YxeiHPC//JcfsAyIvcAAAAAAAAAAAAAAAAEM8Wz4HPklb4sEohzwv/yXH7ADD4QsjL//hDzws/+EbPCwDJ7VR/FgAE+GcActxwItDWAjHSADDcIccAkvI74CHXDR+S8jzhUxGS8jvhwQQighD////9vLGS8jzgAfAB+EdukvI83g=="]
      (is (-> (abi/attach-signature! *context* attach-signature-params)
              :message
              (= expected))))))

(deftest attach-signature-to-message-body-test
  (testing "message body signing"
    (let [encode-message-body-params {:abi events-abi
                                      :call_set {:function_name "returnValue"
                                                 :header {:pubkey (:public tt/keypair)
                                                          :time 1599458364291
                                                          :expire 1599458404}
                                                 :input {:id 0}}
                                      :is_internal false
                                      :signer external-signer}
          body (-> (abi/encode-message-body! *context* encode-message-body-params)
                   :body)
          nacl-sign-detached-params {:unsigned body
                                     :secret (str (:secret tt/keypair) (:public tt/keypair))}
          signature (-> (crypto/nacl-sign-detached! *context* nacl-sign-detached-params)
                        :signature)
          params {:abi events-abi
                  :public_key (:public tt/keypair)
                  :message body
                  :signature signature}
          expected "te6ccgEBAgEAlgAB4b8jS1IezemNgGrVnzkIxeOxxWRk2uithC4Ya6n6dSaFkqxwNokC5L6IXGGdNkE41utoA/yj1bSwm4amJtilh4PE0xnkQqgvUQQ4LYjedUXrxPfmboEdkvKBuC6hsc2uAoAAAF0ZyXLg19VzGQVviwSgAQBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="]
      (is (-> (abi/attach-signature-to-message-body! *context* params)
              :body
              (= expected))))))

(deftest decode-message-test
  (testing "message decoding"
    (let [encoded (tt/create-encoded-message *context* keys-signer)
          params {:abi events-abi
                  :message (:message encoded)}]
      (is (-> (abi/decode-message! *context* params)
              :name
              (= "constructor"))))))

(deftest decode-message-body-test
  (testing "message body decoding"
    (let [params {:abi events-abi
                  :body "te6ccgEBAgEAlgAB4eb6eSBDZAg2YZ4IJ5P+cReLJ2jL1KmQPkzEKKsLLaZRiYUzUBzHX7IgJ0ZqQUGt44+ckKJ1BLDWadBa7O7OQALE0xnkQqgvUQQ4LYjedUXrxPfmboEdkvKBuC6hsc2uAoAAAF0ZyXLg19VzGQVviwSgAQBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
                  :is_internal false}
          expected {:body_type "Input"
                    :header {:pubkey "134c67910aa0bd4410e0b62379d517af13df99ba04764bca06e0ba86c736b80a"
                             :time 1599458364291
                             :expire 1599458404}
                    :name "returnValue"
                    :value {:id 0}}]
      (is (-> (abi/decode-message-body! *context* params)
              (update-in [:value :id] read-string) ; to convert hex string to number
              (= expected))))))

