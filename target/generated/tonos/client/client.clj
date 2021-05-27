
(ns tonos.client.client
  (:require [tonos.client.core :refer [request]]))

(def get-api-reference
  (partial request "client.get_api_reference"))
(defn get-api-reference!
  [& args]
  (-> (apply get-api-reference args)
      doall
      last
      :params-json))

(def version
  (partial request "client.version"))
(defn version!
  [& args]
  (-> (apply version args)
      doall
      last
      :params-json))

(def build-info
  (partial request "client.build_info"))
(defn build-info!
  [& args]
  (-> (apply build-info args)
      doall
      last
      :params-json))

(def resolve-app-request
  (partial request "client.resolve_app_request"))
(defn resolve-app-request!
  [& args]
  (-> (apply resolve-app-request args)
      doall
      last
      :params-json))

