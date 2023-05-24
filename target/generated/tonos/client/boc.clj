
(ns tonos.client.boc
  (:require [tonos.client.core :refer [request]]))

(def decode-tvc
  (partial request "boc.decode_tvc"))
(defn decode-tvc!
  [& args]
  (-> (apply decode-tvc args)
      doall
      last
      :params-json))

(def parse-message
  (partial request "boc.parse_message"))
(defn parse-message!
  [& args]
  (-> (apply parse-message args)
      doall
      last
      :params-json))

(def parse-transaction
  (partial request "boc.parse_transaction"))
(defn parse-transaction!
  [& args]
  (-> (apply parse-transaction args)
      doall
      last
      :params-json))

(def parse-account
  (partial request "boc.parse_account"))
(defn parse-account!
  [& args]
  (-> (apply parse-account args)
      doall
      last
      :params-json))

(def parse-block
  (partial request "boc.parse_block"))
(defn parse-block!
  [& args]
  (-> (apply parse-block args)
      doall
      last
      :params-json))

(def parse-shardstate
  (partial request "boc.parse_shardstate"))
(defn parse-shardstate!
  [& args]
  (-> (apply parse-shardstate args)
      doall
      last
      :params-json))

(def get-blockchain-config
  (partial request "boc.get_blockchain_config"))
(defn get-blockchain-config!
  [& args]
  (-> (apply get-blockchain-config args)
      doall
      last
      :params-json))

(def get-boc-hash
  (partial request "boc.get_boc_hash"))
(defn get-boc-hash!
  [& args]
  (-> (apply get-boc-hash args)
      doall
      last
      :params-json))

(def get-boc-depth
  (partial request "boc.get_boc_depth"))
(defn get-boc-depth!
  [& args]
  (-> (apply get-boc-depth args)
      doall
      last
      :params-json))

(def get-code-from-tvc
  (partial request "boc.get_code_from_tvc"))
(defn get-code-from-tvc!
  [& args]
  (-> (apply get-code-from-tvc args)
      doall
      last
      :params-json))

(def cache-get
  (partial request "boc.cache_get"))
(defn cache-get!
  [& args]
  (-> (apply cache-get args)
      doall
      last
      :params-json))

(def cache-set
  (partial request "boc.cache_set"))
(defn cache-set!
  [& args]
  (-> (apply cache-set args)
      doall
      last
      :params-json))

(def cache-unpin
  (partial request "boc.cache_unpin"))
(defn cache-unpin!
  [& args]
  (-> (apply cache-unpin args)
      doall
      last
      :params-json))

(def encode-boc
  (partial request "boc.encode_boc"))
(defn encode-boc!
  [& args]
  (-> (apply encode-boc args)
      doall
      last
      :params-json))

(def get-code-salt
  (partial request "boc.get_code_salt"))
(defn get-code-salt!
  [& args]
  (-> (apply get-code-salt args)
      doall
      last
      :params-json))

(def set-code-salt
  (partial request "boc.set_code_salt"))
(defn set-code-salt!
  [& args]
  (-> (apply set-code-salt args)
      doall
      last
      :params-json))

(def decode-state-init
  (partial request "boc.decode_state_init"))
(defn decode-state-init!
  [& args]
  (-> (apply decode-state-init args)
      doall
      last
      :params-json))

(def encode-state-init
  (partial request "boc.encode_state_init"))
(defn encode-state-init!
  [& args]
  (-> (apply encode-state-init args)
      doall
      last
      :params-json))

(def encode-external-in-message
  (partial request "boc.encode_external_in_message"))
(defn encode-external-in-message!
  [& args]
  (-> (apply encode-external-in-message args)
      doall
      last
      :params-json))

(def get-compiler-version
  (partial request "boc.get_compiler_version"))
(defn get-compiler-version!
  [& args]
  (-> (apply get-compiler-version args)
      doall
      last
      :params-json))

