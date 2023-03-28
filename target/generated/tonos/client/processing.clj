
(ns tonos.client.processing
  (:require [tonos.client.core :refer [request]]))

(def monitor-messages
  (partial request "processing.monitor_messages"))
(defn monitor-messages!
  [& args]
  (-> (apply monitor-messages args)
      doall
      last
      :params-json))

(def get-monitor-info
  (partial request "processing.get_monitor_info"))
(defn get-monitor-info!
  [& args]
  (-> (apply get-monitor-info args)
      doall
      last
      :params-json))

(def fetch-next-monitor-results
  (partial request "processing.fetch_next_monitor_results"))
(defn fetch-next-monitor-results!
  [& args]
  (-> (apply fetch-next-monitor-results args)
      doall
      last
      :params-json))

(def cancel-monitor
  (partial request "processing.cancel_monitor"))
(defn cancel-monitor!
  [& args]
  (-> (apply cancel-monitor args)
      doall
      last
      :params-json))

(def send-messages
  (partial request "processing.send_messages"))
(defn send-messages!
  [& args]
  (-> (apply send-messages args)
      doall
      last
      :params-json))

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

