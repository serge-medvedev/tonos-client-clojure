(ns tonos.client.test-tools
  (:require
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [tonos.client.abi :as abi]
    [tonos.client.boc :as boc]
    [tonos.client.net :as net]
    [tonos.client.processing :as processing]))

(defn- read-json-rc
  [f]
  (try
    (-> f io/resource slurp (json/read-str :key-fn keyword))
    (catch Exception _ nil)))

(def ^:const dev-net-config "{\"network\":{\"server_address\":\"https://net.ton.dev\"}}")
(def ^:const main-net-config "{\"network\":{\"server_address\":\"https://main.ton.dev\"}}")
(def test-data (read-json-rc "test-data.json"))
(def funding-wallet (read-json-rc "funding-wallet.json"))
(def keypair {:public "134c67910aa0bd4410e0b62379d517af13df99ba04764bca06e0ba86c736b80a",
              :secret "ddf87be7c470ea26811e5ef86391cb97d79afb35098753c2f990c2b0aef5223d"})

(defn create-encoded-message
  ([context signer]
   (create-encoded-message context signer nil nil))
  ([context signer t expire]
   (let [pubkey (cond (= (:type signer) "Keys") (-> signer :keys :public)
                      (= (:type signer) "External") (:public_key signer))
         params {:abi {:type "Contract"
                       :value (-> test-data :events :abi)}
                 :deploy_set {:tvc (-> test-data :events :tvc)}
                 :call_set {:function_name "constructor"
                            :header {:pubkey pubkey
                                     :time t
                                     :expire expire}}
                 :signer signer}]
     (abi/encode-message! context params))))

(defn fund-account
  ([context account]
   (fund-account context account nil))
  ([context account value]
   (when (nil? funding-wallet)
     (throw (Exception. "funding wallet config is missing - \"paid\" tests can't be run")))

   (let [params {:message_encode_params {:abi {:type "Contract"
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
         (let [m (-> (boc/parse-message! context {:boc boc})
                     :parsed)
               params {:collection "transactions"
                       :filter {:in_msg {:eq (:id m)}}
                       :result "id"
                       :timeout 60000}]
           (when (-> m :msg_type_name (= "internal"))
             (net/wait-for-collection! context params))))))))

(defn fetch-account
  [context account]
  (let [params {:collection "accounts"
                :filter {:id {:eq account}}
                :result "id boc"
                :timeout 60000}]
    (-> (net/wait-for-collection! context params)
        :result)))

