
(ns tonos.client.tvm
  (:require [tonos.client.core :refer [request]]))

(def run-executor
  (partial request "tvm.run_executor"))
(defn run-executor!
  [& args]
  (-> (apply run-executor args)
      doall
      last
      :params-json))

(def run-tvm
  (partial request "tvm.run_tvm"))
(defn run-tvm!
  [& args]
  (-> (apply run-tvm args)
      doall
      last
      :params-json))

(def run-get
  (partial request "tvm.run_get"))
(defn run-get!
  [& args]
  (-> (apply run-get args)
      doall
      last
      :params-json))

