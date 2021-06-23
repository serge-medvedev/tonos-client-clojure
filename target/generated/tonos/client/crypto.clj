
(ns tonos.client.crypto
  (:require [tonos.client.core :refer [request]]))

(def factorize
  (partial request "crypto.factorize"))
(defn factorize!
  [& args]
  (-> (apply factorize args)
      doall
      last
      :params-json))

(def modular-power
  (partial request "crypto.modular_power"))
(defn modular-power!
  [& args]
  (-> (apply modular-power args)
      doall
      last
      :params-json))

(def ton-crc16
  (partial request "crypto.ton_crc16"))
(defn ton-crc16!
  [& args]
  (-> (apply ton-crc16 args)
      doall
      last
      :params-json))

(def generate-random-bytes
  (partial request "crypto.generate_random_bytes"))
(defn generate-random-bytes!
  [& args]
  (-> (apply generate-random-bytes args)
      doall
      last
      :params-json))

(def convert-public-key-to-ton-safe-format
  (partial request "crypto.convert_public_key_to_ton_safe_format"))
(defn convert-public-key-to-ton-safe-format!
  [& args]
  (-> (apply convert-public-key-to-ton-safe-format args)
      doall
      last
      :params-json))

(def generate-random-sign-keys
  (partial request "crypto.generate_random_sign_keys"))
(defn generate-random-sign-keys!
  [& args]
  (-> (apply generate-random-sign-keys args)
      doall
      last
      :params-json))

(def sign
  (partial request "crypto.sign"))
(defn sign!
  [& args]
  (-> (apply sign args)
      doall
      last
      :params-json))

(def verify-signature
  (partial request "crypto.verify_signature"))
(defn verify-signature!
  [& args]
  (-> (apply verify-signature args)
      doall
      last
      :params-json))

(def sha256
  (partial request "crypto.sha256"))
(defn sha256!
  [& args]
  (-> (apply sha256 args)
      doall
      last
      :params-json))

(def sha512
  (partial request "crypto.sha512"))
(defn sha512!
  [& args]
  (-> (apply sha512 args)
      doall
      last
      :params-json))

(def scrypt
  (partial request "crypto.scrypt"))
(defn scrypt!
  [& args]
  (-> (apply scrypt args)
      doall
      last
      :params-json))

(def nacl-sign-keypair-from-secret-key
  (partial request "crypto.nacl_sign_keypair_from_secret_key"))
(defn nacl-sign-keypair-from-secret-key!
  [& args]
  (-> (apply nacl-sign-keypair-from-secret-key args)
      doall
      last
      :params-json))

(def nacl-sign
  (partial request "crypto.nacl_sign"))
(defn nacl-sign!
  [& args]
  (-> (apply nacl-sign args)
      doall
      last
      :params-json))

(def nacl-sign-open
  (partial request "crypto.nacl_sign_open"))
(defn nacl-sign-open!
  [& args]
  (-> (apply nacl-sign-open args)
      doall
      last
      :params-json))

(def nacl-sign-detached
  (partial request "crypto.nacl_sign_detached"))
(defn nacl-sign-detached!
  [& args]
  (-> (apply nacl-sign-detached args)
      doall
      last
      :params-json))

(def nacl-sign-detached-verify
  (partial request "crypto.nacl_sign_detached_verify"))
(defn nacl-sign-detached-verify!
  [& args]
  (-> (apply nacl-sign-detached-verify args)
      doall
      last
      :params-json))

(def nacl-box-keypair
  (partial request "crypto.nacl_box_keypair"))
(defn nacl-box-keypair!
  [& args]
  (-> (apply nacl-box-keypair args)
      doall
      last
      :params-json))

(def nacl-box-keypair-from-secret-key
  (partial request "crypto.nacl_box_keypair_from_secret_key"))
(defn nacl-box-keypair-from-secret-key!
  [& args]
  (-> (apply nacl-box-keypair-from-secret-key args)
      doall
      last
      :params-json))

(def nacl-box
  (partial request "crypto.nacl_box"))
(defn nacl-box!
  [& args]
  (-> (apply nacl-box args)
      doall
      last
      :params-json))

(def nacl-box-open
  (partial request "crypto.nacl_box_open"))
(defn nacl-box-open!
  [& args]
  (-> (apply nacl-box-open args)
      doall
      last
      :params-json))

(def nacl-secret-box
  (partial request "crypto.nacl_secret_box"))
(defn nacl-secret-box!
  [& args]
  (-> (apply nacl-secret-box args)
      doall
      last
      :params-json))

(def nacl-secret-box-open
  (partial request "crypto.nacl_secret_box_open"))
(defn nacl-secret-box-open!
  [& args]
  (-> (apply nacl-secret-box-open args)
      doall
      last
      :params-json))

(def mnemonic-words
  (partial request "crypto.mnemonic_words"))
(defn mnemonic-words!
  [& args]
  (-> (apply mnemonic-words args)
      doall
      last
      :params-json))

(def mnemonic-from-random
  (partial request "crypto.mnemonic_from_random"))
(defn mnemonic-from-random!
  [& args]
  (-> (apply mnemonic-from-random args)
      doall
      last
      :params-json))

(def mnemonic-from-entropy
  (partial request "crypto.mnemonic_from_entropy"))
(defn mnemonic-from-entropy!
  [& args]
  (-> (apply mnemonic-from-entropy args)
      doall
      last
      :params-json))

(def mnemonic-verify
  (partial request "crypto.mnemonic_verify"))
(defn mnemonic-verify!
  [& args]
  (-> (apply mnemonic-verify args)
      doall
      last
      :params-json))

(def mnemonic-derive-sign-keys
  (partial request "crypto.mnemonic_derive_sign_keys"))
(defn mnemonic-derive-sign-keys!
  [& args]
  (-> (apply mnemonic-derive-sign-keys args)
      doall
      last
      :params-json))

(def hdkey-xprv-from-mnemonic
  (partial request "crypto.hdkey_xprv_from_mnemonic"))
(defn hdkey-xprv-from-mnemonic!
  [& args]
  (-> (apply hdkey-xprv-from-mnemonic args)
      doall
      last
      :params-json))

(def hdkey-derive-from-xprv
  (partial request "crypto.hdkey_derive_from_xprv"))
(defn hdkey-derive-from-xprv!
  [& args]
  (-> (apply hdkey-derive-from-xprv args)
      doall
      last
      :params-json))

(def hdkey-derive-from-xprv-path
  (partial request "crypto.hdkey_derive_from_xprv_path"))
(defn hdkey-derive-from-xprv-path!
  [& args]
  (-> (apply hdkey-derive-from-xprv-path args)
      doall
      last
      :params-json))

(def hdkey-secret-from-xprv
  (partial request "crypto.hdkey_secret_from_xprv"))
(defn hdkey-secret-from-xprv!
  [& args]
  (-> (apply hdkey-secret-from-xprv args)
      doall
      last
      :params-json))

(def hdkey-public-from-xprv
  (partial request "crypto.hdkey_public_from_xprv"))
(defn hdkey-public-from-xprv!
  [& args]
  (-> (apply hdkey-public-from-xprv args)
      doall
      last
      :params-json))

(def chacha20
  (partial request "crypto.chacha20"))
(defn chacha20!
  [& args]
  (-> (apply chacha20 args)
      doall
      last
      :params-json))

(def register-signing-box
  (partial request "crypto.register_signing_box"))
(defn register-signing-box!
  [& args]
  (-> (apply register-signing-box args)
      doall
      last
      :params-json))

(def get-signing-box
  (partial request "crypto.get_signing_box"))
(defn get-signing-box!
  [& args]
  (-> (apply get-signing-box args)
      doall
      last
      :params-json))

(def signing-box-get-public-key
  (partial request "crypto.signing_box_get_public_key"))
(defn signing-box-get-public-key!
  [& args]
  (-> (apply signing-box-get-public-key args)
      doall
      last
      :params-json))

(def signing-box-sign
  (partial request "crypto.signing_box_sign"))
(defn signing-box-sign!
  [& args]
  (-> (apply signing-box-sign args)
      doall
      last
      :params-json))

(def remove-signing-box
  (partial request "crypto.remove_signing_box"))
(defn remove-signing-box!
  [& args]
  (-> (apply remove-signing-box args)
      doall
      last
      :params-json))

(def register-encryption-box
  (partial request "crypto.register_encryption_box"))
(defn register-encryption-box!
  [& args]
  (-> (apply register-encryption-box args)
      doall
      last
      :params-json))

(def remove-encryption-box
  (partial request "crypto.remove_encryption_box"))
(defn remove-encryption-box!
  [& args]
  (-> (apply remove-encryption-box args)
      doall
      last
      :params-json))

(def encryption-box-get-info
  (partial request "crypto.encryption_box_get_info"))
(defn encryption-box-get-info!
  [& args]
  (-> (apply encryption-box-get-info args)
      doall
      last
      :params-json))

(def encryption-box-encrypt
  (partial request "crypto.encryption_box_encrypt"))
(defn encryption-box-encrypt!
  [& args]
  (-> (apply encryption-box-encrypt args)
      doall
      last
      :params-json))

(def encryption-box-decrypt
  (partial request "crypto.encryption_box_decrypt"))
(defn encryption-box-decrypt!
  [& args]
  (-> (apply encryption-box-decrypt args)
      doall
      last
      :params-json))

