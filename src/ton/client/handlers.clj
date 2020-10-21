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
            [setchan [int] void]
            [getchan [int] Object]])

(defn response-handler-init
  []
  [[] (atom {})])

(defn response-handler-callback
  [this request-id params-json response-type finished]
  (async/put! (.getchan this request-id)
              {:request-id request-id
               :params-json (-> params-json .toString (json/parse-string true))
               :response-type response-type
               :finished finished})
  (Native/detach false))

(defn response-handler-setchan
  [this request-id]
  (let [k (-> request-id str keyword)]
    (swap! (.state this) update k (fnil identity (async/chan)))))

(defn response-handler-getchan
  [this request-id]
  (let [k (-> request-id str keyword)]
    (-> this .state deref k)))

