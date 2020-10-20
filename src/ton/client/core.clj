(ns ton.client.core
  (:gen-class)
  (:require [cheshire.core :as json]
            [clojure.core.async :as async])
  (:import
    [com.sun.jna NativeLibrary Pointer Structure Callback]
    [ton.client.dto StringData]
    [ton.client.handlers ResponseHandler]))


(def ^com.sun.jna.NativeLibrary tc (NativeLibrary/getInstance "ton_client"))

(def request-id (atom 0))

(defn- next-request-id
  []
  (swap! request-id inc))

(defn create-context
  [config]
  (let [config (StringData$ByValue. config)
        handle (.invoke (.getFunction tc "tc_create_context") Pointer (to-array [config]))
        result (.invoke (.getFunction tc "tc_read_string") ton.client.dto.StringData$ByValue (to-array [handle]))]
    (-> result .toString (json/parse-string true) :result)))

(defn destroy-context
  [context]
  (.invoke (.getFunction tc "tc_destroy_context") Void (to-array [context])))

(defn- lazy-seq-builder
  [c]
  (lazy-seq
    (when-some [v (async/<!! c)]
      (let [tail (if (-> v :finished true?) nil (lazy-seq-builder c))]
        (cons v tail)))))

(defn request
  [context function-name function-params-json]
  (let [response-handler (ResponseHandler.)]
    (.invoke (.getFunction tc "tc_request")
             Void
             (let [function-name (StringData$ByValue. function-name)
                   function-params-json (StringData$ByValue. function-params-json)
                   request-id (next-request-id)]
               (to-array [context function-name function-params-json request-id response-handler])))
    (lazy-seq-builder (.state response-handler))))

