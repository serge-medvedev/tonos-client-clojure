(ns ton.client.handlers
  (:require [cheshire.core :as json]
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
            [deref [] clojure.lang.LazySeq]])

(defn response-handler-init
  []
  [[] (async/chan)])

(defn response-handler-callback
  [this request-id params-json response-type finished]
  (async/>!! (.state this)
             {:request-id request-id
              :params-json (-> params-json .toString (json/parse-string true))
              :response-type response-type
              :finished finished})
  (Native/detach finished))

