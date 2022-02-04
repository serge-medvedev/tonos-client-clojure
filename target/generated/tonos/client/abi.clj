
(ns tonos.client.abi
  (:require [tonos.client.core :refer [request]]))

(def encode-message-body
  (partial request "abi.encode_message_body"))
(defn encode-message-body!
  [& args]
  (-> (apply encode-message-body args)
      doall
      last
      :params-json))

(def attach-signature-to-message-body
  (partial request "abi.attach_signature_to_message_body"))
(defn attach-signature-to-message-body!
  [& args]
  (-> (apply attach-signature-to-message-body args)
      doall
      last
      :params-json))

(def encode-message
  (partial request "abi.encode_message"))
(defn encode-message!
  [& args]
  (-> (apply encode-message args)
      doall
      last
      :params-json))

(def encode-internal-message
  (partial request "abi.encode_internal_message"))
(defn encode-internal-message!
  [& args]
  (-> (apply encode-internal-message args)
      doall
      last
      :params-json))

(def attach-signature
  (partial request "abi.attach_signature"))
(defn attach-signature!
  [& args]
  (-> (apply attach-signature args)
      doall
      last
      :params-json))

(def decode-message
  (partial request "abi.decode_message"))
(defn decode-message!
  [& args]
  (-> (apply decode-message args)
      doall
      last
      :params-json))

(def decode-message-body
  (partial request "abi.decode_message_body"))
(defn decode-message-body!
  [& args]
  (-> (apply decode-message-body args)
      doall
      last
      :params-json))

(def encode-account
  (partial request "abi.encode_account"))
(defn encode-account!
  [& args]
  (-> (apply encode-account args)
      doall
      last
      :params-json))

(def decode-account-data
  (partial request "abi.decode_account_data"))
(defn decode-account-data!
  [& args]
  (-> (apply decode-account-data args)
      doall
      last
      :params-json))

(def update-initial-data
  (partial request "abi.update_initial_data"))
(defn update-initial-data!
  [& args]
  (-> (apply update-initial-data args)
      doall
      last
      :params-json))

(def encode-initial-data
  (partial request "abi.encode_initial_data"))
(defn encode-initial-data!
  [& args]
  (-> (apply encode-initial-data args)
      doall
      last
      :params-json))

(def decode-initial-data
  (partial request "abi.decode_initial_data"))
(defn decode-initial-data!
  [& args]
  (-> (apply decode-initial-data args)
      doall
      last
      :params-json))

(def decode-boc
  (partial request "abi.decode_boc"))
(defn decode-boc!
  [& args]
  (-> (apply decode-boc args)
      doall
      last
      :params-json))

(def encode-boc
  (partial request "abi.encode_boc"))
(defn encode-boc!
  [& args]
  (-> (apply encode-boc args)
      doall
      last
      :params-json))

