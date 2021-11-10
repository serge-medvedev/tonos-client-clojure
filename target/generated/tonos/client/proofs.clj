
(ns tonos.client.proofs
  (:require [tonos.client.core :refer [request]]))

(def proof-block-data
  (partial request "proofs.proof_block_data"))
(defn proof-block-data!
  [& args]
  (-> (apply proof-block-data args)
      doall
      last
      :params-json))

(def proof-transaction-data
  (partial request "proofs.proof_transaction_data"))
(defn proof-transaction-data!
  [& args]
  (-> (apply proof-transaction-data args)
      doall
      last
      :params-json))

