(ns tonos.client.crypto-test
  (:require
    [clojure.test :refer :all]
    [tonos.client.test-tools :as tt]
    [clojure.string :as string]
    [tonos.client.crypto :as crypto]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)
(def ^:dynamic *unsigned* nil)

(def ^:const signed "/OE+67uuV/OHBFkVq0PLxUu7oWUlnV3TdEzjlnkz8yC5i5blGQDuxgJxR81idAwpASeHXhFW82YoojRbotKmBLXunHIBAhcBAANoAAKniABnZBHo62O5aNgkdvrHxcq27e5QqqlPEJBFusYHFDUhTBGRNMZ5EKoL1EEOC2I3nVF68T35m6BHZLygbguobHNrgKAAABdGcly4NfVcxkaLVfP4BgEBAcACAgPPIAUDAQHeBAAD0CAAQdiaYzyIVQXqIIcFsRvOqL14nvzN0COyXlA3BdQ2ObXAVAIm/wD0pCAiwAGS9KDhiu1TWDD0oQkHAQr0pCD0oQgAAAIBIAwKAcj/fyHtRNAg10nCAY4Q0//TP9MA0X/4Yfhm+GP4Yo4Y9AVwAYBA9A7yvdcL//hicPhjcPhmf/hh4tMAAY4dgQIA1xgg+QEB0wABlNP/AwGTAvhC4iD4ZfkQ8qiV0wAB8nri0z8BCwBqjh74QyG5IJ8wIPgjgQPoqIIIG3dAoLnekvhj4IA08jTY0x8B+CO88rnTHwHwAfhHbpLyPN4CASASDQIBIA8OAL26i1Xz/4QW6ONe1E0CDXScIBjhDT/9M/0wDRf/hh+Gb4Y/hijhj0BXABgED0DvK91wv/+GJw+GNw+GZ/+GHi3vhG8nNx+GbR+AD4QsjL//hDzws/+EbPCwDJ7VR/+GeAIBIBEQAOW4gAa1vwgt0cJ9qJoaf/pn+mAaL/8MPwzfDH8MW99IMrqaOh9IG/o/CKQN0kYOG98IV15cDJ8AGRk/YIQZGfChGdGggQH0AAAAAAAAAAAAAAAAAAgZ4tkwIBAfYAYfCFkZf/8IeeFn/wjZ4WAZPaqP/wzwAMW5k8Ki3wgt0cJ9qJoaf/pn+mAaL/8MPwzfDH8MW9rhv/K6mjoaf/v6PwAZEXuAAAAAAAAAAAAAAAACGeLZ8DnyOPLGL0Q54X/5Lj9gBh8IWRl//wh54Wf/CNnhYBk9qo//DPACAUgWEwEJuLfFglAUAfz4QW6OE+1E0NP/0z/TANF/+GH4Zvhj+GLe1w3/ldTR0NP/39H4AMiL3AAAAAAAAAAAAAAAABDPFs+Bz5HHljF6Ic8L/8lx+wDIi9wAAAAAAAAAAAAAAAAQzxbPgc+SVviwSiHPC//JcfsAMPhCyMv/+EPPCz/4Rs8LAMntVH8VAAT4ZwBy3HAi0NYCMdIAMNwhxwCS8jvgIdcNH5LyPOFTEZLyO+HBBCKCEP////28sZLyPOAB8AH4R26S8jze")

(defn- is-hex-string
  [s]
  (re-matches #"^[0-9a-f]+$" s))

(defn- tf
  [f]
  (binding [*context* (core/create-context tt/dev-net-config)]
    (let [signer {:type "External"
                  :public_key (:public tt/keypair)}]
      (binding [*unsigned* (tt/create-encoded-message *context* signer 1599458364291 1599458404)]
        (f)
        (core/destroy-context *context*)))))

(use-fixtures :once tf)

(deftest hdkey-derive-from-xprv-path-test
  (testing "getting a derived xprv"
    (let [params {:xprv "xprv9s21ZrQH143K3PBHjRFACygw9K1AA8k2jTRkEDbuHX8MfTP4XxifWQj1xqqxsk8tkxCNgkJGQZUz8R5zQytHL3Kkco5WAhDB89i9r38DBrt"
                  :path "m/44'/396'/0'/0/0"}]
      (is (-> (crypto/hdkey-derive-from-xprv-path *context* params)
              doall
              last
              :params-json
              :xprv
              (= "xprvA34PuqhZ7YpNA6kJHPV6xwo78p7z9GbGYEogQLvvMRu2UaoLN3CYDT6bkC9LdavcHrBuyoncoTjQVqCAWjXakP2emHxjM4gEJ2ixtUsBZQz"))))))

(deftest hdkey-public-from-xprv-test
  (testing "getting a public key"
    (let [params {:xprv "xprv9s21ZrQH143K3PBHjRFACygw9K1AA8k2jTRkEDbuHX8MfTP4XxifWQj1xqqxsk8tkxCNgkJGQZUz8R5zQytHL3Kkco5WAhDB89i9r38DBrt"}]
      (is (-> (crypto/hdkey-public-from-xprv *context* params)
              doall
              last
              :params-json
              :public
              (= "033675d6aaa8ebef3adab9ac79af58eb975b10dd46067c904747018b46bc88f956"))))))

(deftest hdkey-secret-from-xprv-test
  (testing "getting a secret key"
    (let [params {:xprv "xprv9s21ZrQH143K3PBHjRFACygw9K1AA8k2jTRkEDbuHX8MfTP4XxifWQj1xqqxsk8tkxCNgkJGQZUz8R5zQytHL3Kkco5WAhDB89i9r38DBrt"}]
      (is (-> (crypto/hdkey-secret-from-xprv *context* params)
              doall
              last
              :params-json
              :secret
              (= "f393555dcc9657f22b3c309f8004b364e3f04de6db22d0a32ceff17637327099"))))))

(deftest hdkey-xprv-from-mnemonic-test
  (testing "getting an xprv"
    (let [params {:phrase "dawn fee flip salute width fancy prevent income early planet uphold boost travel concert explain"}
          expected "xprv9s21ZrQH143K3PBHjRFACygw9K1AA8k2jTRkEDbuHX8MfTP4XxifWQj1xqqxsk8tkxCNgkJGQZUz8R5zQytHL3Kkco5WAhDB89i9r38DBrt"]
      (is (-> (crypto/hdkey-xprv-from-mnemonic *context* params)
              doall
              last
              :params-json
              :xprv
              (= expected))))))

(deftest hdkey-derive-from-xprv-test
  (testing "getting a derived key"
    (let [params {:xprv "xprv9s21ZrQH143K3PBHjRFACygw9K1AA8k2jTRkEDbuHX8MfTP4XxifWQj1xqqxsk8tkxCNgkJGQZUz8R5zQytHL3Kkco5WAhDB89i9r38DBrt"
                  :child_index 0x80000000
                  :hardened true}]
      (is (-> (crypto/hdkey-derive-from-xprv *context* params)
              doall
              last
              :params-json
              :xprv
              (= "xprv9umdhXwHgt9GyXA6NRKgaj9CeRPNgF6kZHnt49GQNTxoFuZ18CoLxHW22SkU7FoUfSa6eoirTVVtv7rkKeAobNPZ2FTQVbtdZ36qXdCqWfc"))))))

(deftest nacl-box-test
  (testing "getting an encrypted data"
    (let [params {:decrypted "VE9OIFNESyB2MS4wLjA="
                  :nonce "8618a02d351f0ce5f6bc0f56674e977a6a896a2cbb35d279" ; must be a 24-byte hex string
                  :their_public "9950d2f1a3cee9fcbc6614aba64636215c31edee31061de77c93d2fa62a67732"
                  :secret "c9332e3f09c8de109122ea7ce992e579e475f7b1ae8d70a0e2bd911f8ffb0ec4"}]
      (is (-> (crypto/nacl-box *context* params)
              doall
              last
              :params-json
              :encrypted
              (= "GcIo9A7BWvjvmacO6iNDk5pPUFs6jY43T8mkzoKC"))))))

(deftest nacl-sign-detached-test
  (testing "getting a signature"
    (let [params {:unsigned (:message *unsigned*)
                  :secret (str (:secret tt/keypair) (:public tt/keypair))}]
      (is (-> (crypto/nacl-sign-detached *context* params)
              doall
              last
              :params-json
              :signature
              (= "fce13eebbbae57f387045915ab43cbc54bbba165259d5dd3744ce3967933f320b98b96e51900eec6027147cd62740c290127875e1156f36628a2345ba2d2a604"))))))

(deftest nacl-box-keypair-test
  (testing "getting a random key pair"
    (let [result (-> (crypto/nacl-box-keypair *context*)
                     doall
                     last
                     :params-json)]
      (is (-> result
              :public
              is-hex-string))
      (is (-> result
              :secret
              is-hex-string)))))

(deftest nacl-sign-keypair-from-secret-key-test
  (testing "getting a signed key pair"
    (let [params {:secret (:secret tt/keypair)}
          expected {:public "134c67910aa0bd4410e0b62379d517af13df99ba04764bca06e0ba86c736b80a"
                    :secret "ddf87be7c470ea26811e5ef86391cb97d79afb35098753c2f990c2b0aef5223d134c67910aa0bd4410e0b62379d517af13df99ba04764bca06e0ba86c736b80a"}]
      (is (-> (crypto/nacl-sign-keypair-from-secret-key *context* params)
              doall
              last
              :params-json
              (= expected))))))

(deftest nacl-box-open-test
  (testing "getting decrypted data"
    (let [params {:encrypted "GcIo9A7BWvjvmacO6iNDk5pPUFs6jY43T8mkzoKC"
                  :nonce "8618a02d351f0ce5f6bc0f56674e977a6a896a2cbb35d279" ; must be a 24-byte hex string
                  :their_public "b8e902c15096bf030fc8a0b3549bca15ca4bc74c3612964a72d93a2b00420308"
                  :secret "56fb22f277b9aee361f925b67bc97a31e302c20e4468b6c80fb1f840a12d6349"}]
      (is (-> (crypto/nacl-box-open *context* params)
              doall
              last
              :params-json
              :decrypted
              (= "VE9OIFNESyB2MS4wLjA="))))))

(deftest nacl-secret-box-test
  (testing "getting an encrypted data"
    (let [params {:decrypted "VE9OIFNESyB2MS4wLjA="
                  :nonce "8618a02d351f0ce5f6bc0f56674e977a6a896a2cbb35d279" ; must be a 24-byte hex string
                  :key "c9332e3f09c8de109122ea7ce992e579e475f7b1ae8d70a0e2bd911f8ffb0ec4"}]
      (is (-> (crypto/nacl-secret-box *context* params)
              doall
              last
              :params-json
              :encrypted
              (= "H0+O0nk3Tedp+Dd7Fp3kA8HH0f54TUyQdPb25PII"))))))

(deftest nacl-sign-open-test
  (testing "getting unsigned message"
    (let [params {:signed signed
                  :public (:public tt/keypair)}]
      (is (-> (crypto/nacl-sign-open *context* params)
              doall
              last
              :params-json
              :unsigned
              (= (:message *unsigned*)))))))

(deftest nacl-box-keypair-from-secret-key-test
  (testing "getting a key pair"
    (let [params {:secret (:secret tt/keypair)}]
      (is (-> (crypto/nacl-box-keypair-from-secret-key *context* params)
              doall
              last
              :params-json
              :secret
              (= (:secret params)))))))

(deftest nacl-secret-box-open-test
  (testing "data decryption"
    (let [params {:encrypted "H0+O0nk3Tedp+Dd7Fp3kA8HH0f54TUyQdPb25PII"
                  :nonce "8618a02d351f0ce5f6bc0f56674e977a6a896a2cbb35d279" ; must be a 24-byte hex string
                  :key "c9332e3f09c8de109122ea7ce992e579e475f7b1ae8d70a0e2bd911f8ffb0ec4"}]
      (is (-> (crypto/nacl-secret-box-open *context* params)
              doall
              last
              :params-json
              :decrypted
              (= "VE9OIFNESyB2MS4wLjA="))))))

(deftest nacl-sign-test
  (testing "data signing"
    (let [params {:unsigned (:message *unsigned*)
                  :secret (str (:secret tt/keypair) (:public tt/keypair))}]
      (is (-> (crypto/nacl-sign *context* params)
              doall
              last
              :params-json
              :signed
              (= signed))))))

(deftest mnemonic-from-random-test
  (testing "getting a mnemonic phrase"
    (let [params {:dictionary 1
                  :word_count 12}]
      (is (-> (crypto/mnemonic-from-random *context* params)
              doall
              last
              :params-json
              :phrase
              (string/split #" ")
              count
              (= 12))))))

(deftest mnemonic-words-test
  (testing "leopard presence"
    (is (-> (crypto/mnemonic-words *context* {:dictionary 1})
            doall
            last
            :params-json
            :words
            (string/includes? "leopard")))))

(deftest mnemonic-derive-sign-keys-test
  (testing "getting a derived key pair"
    (let [params {:phrase "dumb hunt swamp naive range drama snake network pride bag shoot earn"
                  :path "m/44'/396'/0'/0/0"}
          expected {:public "134c67910aa0bd4410e0b62379d517af13df99ba04764bca06e0ba86c736b80a"
                    :secret "ddf87be7c470ea26811e5ef86391cb97d79afb35098753c2f990c2b0aef5223d"}]
      (is (-> (crypto/mnemonic-derive-sign-keys *context* params)
              doall
              last
              :params-json
              (= expected))))))

(deftest mnemonic-from-entropy-test
  (testing "getting a mnemonic phrase"
    (let [params {:entropy "37ea91645f6faca5ea93944534bfbb0cde785d54"
                  :dictionary 1
                  :word_count 15}
          expected "dawn fee flip salute width fancy prevent income early planet uphold boost travel concert explain"]
      (is (-> (crypto/mnemonic-from-entropy *context* params)
              doall
              last
              :params-json
              :phrase
              (= expected))))))

(deftest mnemonic-verify-test
  (testing "if a mnemonic phrase is valid"
    (let [params {:phrase "dawn fee flip salute width fancy prevent income early planet uphold boost travel concert explain"
                  :dictionary 1
                  :word_count 15}]
      (is (-> (crypto/mnemonic-verify *context* params)
              doall
              last
              :params-json
              :valid)))))

(deftest generate-random-sign-keys-test
  (testing "getting a random key pair"
    (let [result (-> (crypto/generate-random-sign-keys *context*)
                     doall
                     last
                     :params-json)]
      (is (-> result
              :public
              is-hex-string))
      (is (-> result
              :secret
              is-hex-string)))))

(deftest generate-random-bytes-test
  (testing "getting a few random bytes"
    (is (-> (crypto/generate-random-bytes *context* {:length 8})
            doall
            last
            :params-json
            :bytes
            count
            (> 0)))))

(deftest verify-signature-test
  (testing "getting unsigned message"
    (let [params {:signed signed
                  :public (:public tt/keypair)}]
      (is (-> (crypto/verify-signature *context* params)
              doall
              last
              :params-json
              :unsigned
              (= (:message *unsigned*)))))))

(deftest sign-test
  (testing "data signing"
    (let [params {:unsigned (:message *unsigned*)
                  :keys tt/keypair}]
      (is (-> (crypto/sign *context* params)
              doall
              last
              :params-json
              :signed
              (= signed))))))

(deftest convert-public-key-to-ton-safe-format-test
  (testing "public key conversion"
    (let [params {:public_key (:public tt/keypair)}]
      (is (-> (crypto/convert-public-key-to-ton-safe-format *context* params)
              doall
              last
              :params-json
              :ton_public_key
              (= "PuYTTGeRCqC9RBDgtiN51RevE9-ZugR2S8oG4LqGxza4Cv_l"))))))

(deftest sha256-test
  (testing "getting sha256 sum"
    (is (-> (crypto/sha256 *context* {:data "VE9OIFNESyB2MS4wLjA="})
            doall
            last
            :params-json
            :hash
            (= "da13ae74bb2a3e817715e1f80a8d43047b5d714aa5701ef6a386bacd7e10e664")))))

(deftest sha512-test
  (testing "getting sha512 sum"
    (is (-> (crypto/sha512 *context* {:data "VE9OIFNESyB2MS4wLjA="})
            doall
            last
            :params-json
            :hash
            (= "0a9814b1d5a0c2bfd8b6cd13b8acff110fd1779d2c8b768c847e3bc7fc93209e6e98b12b53174bb0d7688a4ebba600001883ed5951fa1d49e52483eb15f94c2f")))))

(deftest ton-crc16-test
  (testing "getting a CRS sum"
    (is (-> (crypto/ton-crc16 *context* {:data "VE9OIFNESyB2MS4wLjA="})
            doall
            last
            :params-json
            :crc
            (= 7158)))))

(deftest factorize-test
  (testing "getting factors of a composite"
    (let [params {:composite (format "%016x" 12)}]
      (is (-> (crypto/factorize *context* params)
              doall
              last
              :params-json
              :factors
              (= ["3" "4"])))))
  (testing "getting error if a number cannot be factorized"
    (let [params {:composite (format "%016x" 13)}
          result (-> (crypto/factorize *context* params)
                     doall
                     last
                     :params-json)]
      (is (-> result
              :code
              (= 106)))
      (is (->> result
               :message
               (re-find #"^Invalid factorize challenge.*"))))))

(deftest modular-power-test
  (testing "getting a result of modular exponentiation"
    (let [params {:base "05"
                  :exponent "03"
                  :modulus "0d"}]
      (is (-> (crypto/modular-power *context* params)
              doall
              last
              :params-json
              :modular_power
              (= "8"))))))

(deftest scrypt-test
  (testing "getting a derived key"
    (let [params {:password "cXVlcnR5"
                  :salt "c2FsdA=="
                  :log_n 2
                  :r 8
                  :p 2
                  :dk_len 8}]
      (is (-> (crypto/scrypt *context* params)
              doall
              last
              :params-json
              :key
              (= "bad59a9c2a82ad59"))))))

