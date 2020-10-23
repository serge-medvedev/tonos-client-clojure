(ns ton.client.handlers
  (:require
    [clojure.data.json :as json]
    [clojure.core.async :as async])
  (:import
    [com.sun.jna Callback Native]
    [ton.client.dto StringData]))


(gen-class
  :name ton.client.handlers.ResponseHandler
  :implements [com.sun.jna.Callback AutoCloseable]
  :init init
  :constructors {[] []}
  :state state
  :prefix "response-handler-"
  :methods [[^{Override {}} callback [int ton.client.dto.StringData$ByValue int boolean] void]
            [setchan [int] void]
            [getchan [int] Object]])

(defn response-handler-init
  []
  [[] (atom {})])

(defn response-handler-callback
  [this request-id params-json response-type finished]
  (let [c (.getchan this request-id)]
    (async/put! c {:request-id request-id
                   :params-json (-> params-json
                                    .toString
                                    (json/read-str :key-fn keyword
                                                   :eof-error? false
                                                   :eof-value {}))
                   :response-type response-type
                   :finished finished})
    (when finished
      (async/close! c)
      (swap! (.state this) dissoc request-id)))
  (Native/detach false)) ; Core Library uses a pool of threads to run callbacks,
                         ; so the number of JVM threads staying attached is limited

(defn response-handler-setchan
  [this request-id]
  (swap! (.state this) update request-id (fnil identity (async/chan))))

(defn response-handler-getchan
  [this request-id]
  (-> this .state deref (get request-id)))

