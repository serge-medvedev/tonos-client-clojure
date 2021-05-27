
(ns tonos.client.net
  (:require [tonos.client.core :refer [request]]))

(def query
  (partial request "net.query"))
(defn query!
  [& args]
  (-> (apply query args)
      doall
      last
      :params-json))

(def batch-query
  (partial request "net.batch_query"))
(defn batch-query!
  [& args]
  (-> (apply batch-query args)
      doall
      last
      :params-json))

(def query-collection
  (partial request "net.query_collection"))
(defn query-collection!
  [& args]
  (-> (apply query-collection args)
      doall
      last
      :params-json))

(def aggregate-collection
  (partial request "net.aggregate_collection"))
(defn aggregate-collection!
  [& args]
  (-> (apply aggregate-collection args)
      doall
      last
      :params-json))

(def wait-for-collection
  (partial request "net.wait_for_collection"))
(defn wait-for-collection!
  [& args]
  (-> (apply wait-for-collection args)
      doall
      last
      :params-json))

(def unsubscribe
  (partial request "net.unsubscribe"))
(defn unsubscribe!
  [& args]
  (-> (apply unsubscribe args)
      doall
      last
      :params-json))

(def subscribe-collection
  (partial request "net.subscribe_collection"))

(def suspend
  (partial request "net.suspend"))
(defn suspend!
  [& args]
  (-> (apply suspend args)
      doall
      last
      :params-json))

(def resume
  (partial request "net.resume"))
(defn resume!
  [& args]
  (-> (apply resume args)
      doall
      last
      :params-json))

(def find-last-shard-block
  (partial request "net.find_last_shard_block"))
(defn find-last-shard-block!
  [& args]
  (-> (apply find-last-shard-block args)
      doall
      last
      :params-json))

(def fetch-endpoints
  (partial request "net.fetch_endpoints"))
(defn fetch-endpoints!
  [& args]
  (-> (apply fetch-endpoints args)
      doall
      last
      :params-json))

(def set-endpoints
  (partial request "net.set_endpoints"))
(defn set-endpoints!
  [& args]
  (-> (apply set-endpoints args)
      doall
      last
      :params-json))

(def get-endpoints
  (partial request "net.get_endpoints"))
(defn get-endpoints!
  [& args]
  (-> (apply get-endpoints args)
      doall
      last
      :params-json))

(def query-counterparties
  (partial request "net.query_counterparties"))
(defn query-counterparties!
  [& args]
  (-> (apply query-counterparties args)
      doall
      last
      :params-json))

(def query-transaction-tree
  (partial request "net.query_transaction_tree"))
(defn query-transaction-tree!
  [& args]
  (-> (apply query-transaction-tree args)
      doall
      last
      :params-json))

