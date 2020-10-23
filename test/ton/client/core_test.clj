(ns ton.client.core-test
  (:require
    [clojure.test :refer :all]
    [clojure.data.json :as json]
    [ton.client.abi :as abi]
    [ton.client.client :as client]
    [ton.client.crypto :as crypto]
    [ton.client.processing :as processing]
    [ton.client.core :refer :all]))


(def ^:const config "{\"network\": {\"server_address\": \"https://net.ton.dev\"}}")
(def ^:const events-abi "{\"ABI version\":2,\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"emitValue\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"outputs\":[]},{\"name\":\"returnValue\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"outputs\":[{\"name\":\"value0\",\"type\":\"uint256\"}]},{\"name\":\"sendAllMoney\",\"inputs\":[{\"name\":\"dest_addr\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"constructor\",\"inputs\":[],\"outputs\":[]}],\"data\":[],\"events\":[{\"name\":\"EventThrown\",\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"outputs\":[]}]}")
(def ^:const events-tvc "te6ccgECFwEAAxUAAgE0BgEBAcACAgPPIAUDAQHeBAAD0CAAQdgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAIm/wD0pCAiwAGS9KDhiu1TWDD0oQkHAQr0pCD0oQgAAAIBIAwKAcj/fyHtRNAg10nCAY4Q0//TP9MA0X/4Yfhm+GP4Yo4Y9AVwAYBA9A7yvdcL//hicPhjcPhmf/hh4tMAAY4dgQIA1xgg+QEB0wABlNP/AwGTAvhC4iD4ZfkQ8qiV0wAB8nri0z8BCwBqjh74QyG5IJ8wIPgjgQPoqIIIG3dAoLnekvhj4IA08jTY0x8B+CO88rnTHwHwAfhHbpLyPN4CASASDQIBIA8OAL26i1Xz/4QW6ONe1E0CDXScIBjhDT/9M/0wDRf/hh+Gb4Y/hijhj0BXABgED0DvK91wv/+GJw+GNw+GZ/+GHi3vhG8nNx+GbR+AD4QsjL//hDzws/+EbPCwDJ7VR/+GeAIBIBEQAOW4gAa1vwgt0cJ9qJoaf/pn+mAaL/8MPwzfDH8MW99IMrqaOh9IG/o/CKQN0kYOG98IV15cDJ8AGRk/YIQZGfChGdGggQH0AAAAAAAAAAAAAAAAAAgZ4tkwIBAfYAYfCFkZf/8IeeFn/wjZ4WAZPaqP/wzwAMW5k8Ki3wgt0cJ9qJoaf/pn+mAaL/8MPwzfDH8MW9rhv/K6mjoaf/v6PwAZEXuAAAAAAAAAAAAAAAACGeLZ8DnyOPLGL0Q54X/5Lj9gBh8IWRl//wh54Wf/CNnhYBk9qo//DPACAUgWEwEJuLfFglAUAfz4QW6OE+1E0NP/0z/TANF/+GH4Zvhj+GLe1w3/ldTR0NP/39H4AMiL3AAAAAAAAAAAAAAAABDPFs+Bz5HHljF6Ic8L/8lx+wDIi9wAAAAAAAAAAAAAAAAQzxbPgc+SVviwSiHPC//JcfsAMPhCyMv/+EPPCz/4Rs8LAMntVH8VAAT4ZwBy3HAi0NYCMdIAMNwhxwCS8jvgIdcNH5LyPOFTEZLyO+HBBCKCEP////28sZLyPOAB8AH4R26S8jze")

(defn- create-encoded-message
  ([context signer]
   (create-encoded-message context signer nil nil))
  ([context signer t e]
   (let [pubkey (cond (= (:type signer) "Keys") (-> signer :keys :public)
                      (= (:type signer) "External") (:public_key signer))
         params {:abi {:type "Serialized"
                       :value (json/read-str events-abi :key-fn keyword)}
                 :deploy_set {:tvc events-tvc}
                 :call_set {:function_name "constructor"
                            :header {:pubkey pubkey
                                     :time t
                                     :expire e}}
                 :signer signer}]
     (-> (abi/encode-message context params) doall first :params-json))))

(def ^:dynamic *context* nil)

(defn context-fixture
  [f]
  (binding [*context* (create-context config)]
    (f)
    (destroy-context *context*)))

(use-fixtures :each context-fixture)

(deftest client-version-test
  (testing "getting sdk version asynchronously"
    (is (-> (client/version *context*) doall first :params-json :version (= "1.0.0") true?))))

(deftest processing-send-message-test
  (testing "sending message"
    (let [keypair (-> (crypto/generate-random-sign-keys *context*) doall first :params-json)
          encoded (create-encoded-message *context* {:type "Keys" :keys keypair})
          params {:message (:message encoded) :send_events true}
          results (processing/send-message *context* params)]
      (doseq [x results] (println x)))))

