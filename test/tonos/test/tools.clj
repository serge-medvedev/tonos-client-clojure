(ns tonos.test.tools
  (:require
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [tonos.client.abi :as abi]
    [tonos.client.boc :as boc]
    [tonos.client.net :as net]
    [tonos.client.processing :as processing]))

(defn- read-json-rc
  [f]
  (-> f io/resource slurp (json/read-str :key-fn keyword)))

(def test-data
  (read-json-rc "test-data.json"))

(def funding-wallet
  (read-json-rc "funding-wallet.json"))

(def keypair
  {:public "134c67910aa0bd4410e0b62379d517af13df99ba04764bca06e0ba86c736b80a",
   :secret "ddf87be7c470ea26811e5ef86391cb97d79afb35098753c2f990c2b0aef5223d"})

(defn create-encoded-message
  ([context signer]
   (create-encoded-message context signer nil nil))
  ([context signer t expire]
   (let [pubkey (cond (= (:type signer) "Keys") (-> signer :keys :public)
                      (= (:type signer) "External") (:public_key signer))
         params {:abi {:type "Serialized"
                       :value (-> test-data :events :abi)}
                 :deploy_set {:tvc (-> test-data :events :tvc)}
                 :call_set {:function_name "constructor"
                            :header {:pubkey pubkey
                                     :time t
                                     :expire expire}}
                 :signer signer}]
     (-> (abi/encode-message context params)
         doall
         last
         :params-json))))

(defn fund-account
  ([context account]
   (fund-account context account nil))
  ([context account value]
   (let [params {:message_encode_params {:abi {:type "Serialized"
                                               :value (:abi funding-wallet)}
                                         :address (:address funding-wallet)
                                         :call_set {:function_name "sendTransaction"
                                                    :input {:dest account
                                                            :value (or value 500000000)
                                                            :bounce false
                                                            :flags 0
                                                            :payload ""}}
                                         :signer {:type "Keys" :keys (:keys funding-wallet)}}
                 :send_events false}]
     (doseq [x (processing/process-message context params)]
       (doseq [boc (-> x :params-json :out_messages)]
         (let [m (-> (boc/parse-message context {:boc boc})
                     doall
                     last
                     :params-json
                     :parsed)]
           (when (-> m :msg_type_name (= "internal"))
             (let [params {:collection "transactions"
                           :filter {:in_msg {:eq (:id m)}}
                           :result "id"
                           :timeout 60000}]
               (-> (net/wait-for-collection context params)
                   dorun)))))))))

