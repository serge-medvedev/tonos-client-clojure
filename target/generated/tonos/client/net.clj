
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

(def subscribe
  (partial request "net.subscribe"))
(defn subscribe!
  [& args]
  (-> (apply subscribe args)
      doall
      last
      :params-json))

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

(def create-block-iterator
  (partial request "net.create_block_iterator"))
(defn create-block-iterator!
  [& args]
  (-> (apply create-block-iterator args)
      doall
      last
      :params-json))

(def resume-block-iterator
  (partial request "net.resume_block_iterator"))
(defn resume-block-iterator!
  [& args]
  (-> (apply resume-block-iterator args)
      doall
      last
      :params-json))

(def create-transaction-iterator
  (partial request "net.create_transaction_iterator"))
(defn create-transaction-iterator!
  [& args]
  (-> (apply create-transaction-iterator args)
      doall
      last
      :params-json))

(def resume-transaction-iterator
  (partial request "net.resume_transaction_iterator"))
(defn resume-transaction-iterator!
  [& args]
  (-> (apply resume-transaction-iterator args)
      doall
      last
      :params-json))

(def iterator-next
  (partial request "net.iterator_next"))
(defn iterator-next!
  [& args]
  (-> (apply iterator-next args)
      doall
      last
      :params-json))

(def remove-iterator
  (partial request "net.remove_iterator"))
(defn remove-iterator!
  [& args]
  (-> (apply remove-iterator args)
      doall
      last
      :params-json))

(def get-signature-id
  (partial request "net.get_signature_id"))
(defn get-signature-id!
  [& args]
  (-> (apply get-signature-id args)
      doall
      last
      :params-json))

