(ns tonos.client.core
  (:gen-class)
  (:require
    [clojure.data.json :as json]
    [clojure.core.async :as async :refer [<!!]])
  (:import
    [com.sun.jna NativeLibrary Pointer]
    [tonos.client.handlers ResponseHandler]))


(def ^com.sun.jna.NativeLibrary tc (NativeLibrary/getInstance "ton_client"))
(def request-id (atom 0))
(def response-handler (ResponseHandler.))

(defn create-context
  [config]
  (let [config (tonos.client.dto.StringData$ByValue. config)
        handle (.invoke (.getFunction tc "tc_create_context") Pointer (to-array [config]))
        result (.invoke (.getFunction tc "tc_read_string") tonos.client.dto.StringData$ByValue (to-array [handle]))]
    (-> result .toString (json/read-str :key-fn keyword) :result)))

(defn destroy-context
  [context]
  (.invoke (.getFunction tc "tc_destroy_context") Void (to-array [context])))

(defn- next-request-id
  []
  (swap! request-id inc))

(defn- lazy-seq-builder
  [c]
  (lazy-seq
    (when-some [v (<!! c)]
      (cons v (if (:finished v) nil (lazy-seq-builder c))))))

(defn request
  ([function-name context]
    (request function-name context {}))
  ([function-name context function-params]
    (let [request-id (next-request-id)]
      (.setchan response-handler request-id)
      (.invoke (.getFunction tc "tc_request")
               Void
               (let [function-name (tonos.client.dto.StringData$ByValue. function-name)
                     function-params-json (-> function-params
                                              json/write-str
                                              tonos.client.dto.StringData$ByValue.)]
                 (to-array [context function-name function-params-json request-id response-handler])))
      (lazy-seq-builder (.getchan response-handler request-id)))))

