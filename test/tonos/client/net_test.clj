(ns tonos.client.net-test
  (:require
    [clojure.test :refer :all]
    [tonos.client.test-tools :as tt]
    [tonos.client.net :as net]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)

(defn context-fixture
  [f]
  (binding [*context* (core/create-context tt/main-net-config)]
    (f)
    (core/destroy-context *context*)))

(use-fixtures :each context-fixture)

(deftest query-collection-test
  (testing "account balance retrieval"
    (let [addr "0:7866e5e4edc40639331140807d2a2dc7d4bc53005bb34d71428cdd250c91b404"
          params {:collection "accounts"
                  :filter {:id {:eq addr}}
                  :result "balance"}]
      (is (-> (net/query-collection *context* params)
              doall
              last
              :params-json
              :result
              first
              :balance
              read-string
              (> 0))))))

(deftest ^:slow subscribe-collection-test
  (testing "receiving incoming message being subscribed"
    (let [params {:collection "messages"
                  :result "id"}
          results (->> (net/subscribe-collection *context* params)
                       (take 5) ; let's consider the first 5 events
                       doall)
          subscription-handle (-> results first :params-json :handle)]
      (is (-> subscription-handle (> 0)))
      (is (->> results
               rest
               (every? #(-> % :params-json :result :id string?))))
      (net/unsubscribe *context* {:handle subscription-handle}))))

(deftest wait-for-collection-test
  (testing "waiting for an incoming message"
    (let [params {:collection "messages"
                  :result "id"
                  :timeout 10000}]
      (is (->> (net/wait-for-collection *context* params)
               doall
               last
               :params-json
               :result
               :id
               (re-matches #"^[0-9a-f]+$"))))))

