
(ns tonos.client.utils
  (:require [tonos.client.core :refer [request]]))

(def convert-address
  (partial request "utils.convert_address"))
(defn convert-address!
  [& args]
  (-> (apply convert-address args)
      doall
      last
      :params-json))

(def calc-storage-fee
  (partial request "utils.calc_storage_fee"))
(defn calc-storage-fee!
  [& args]
  (-> (apply calc-storage-fee args)
      doall
      last
      :params-json))

(def compress-zstd
  (partial request "utils.compress_zstd"))
(defn compress-zstd!
  [& args]
  (-> (apply compress-zstd args)
      doall
      last
      :params-json))

(def decompress-zstd
  (partial request "utils.decompress_zstd"))
(defn decompress-zstd!
  [& args]
  (-> (apply decompress-zstd args)
      doall
      last
      :params-json))

