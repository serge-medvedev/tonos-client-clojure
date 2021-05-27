
(ns tonos.client.processing
  (:require [tonos.client.core :refer [request]]))

(def send-message
  (partial request "processing.send_message"))

(def wait-for-transaction
  (partial request "processing.wait_for_transaction"))

(def process-message
  (partial request "processing.process_message"))
(defn process-message!
  [& args]
  (-> (apply process-message args)
      doall
      last
      :params-json))

