
(ns tonos.client.debot
  (:require [tonos.client.core :refer [request]]))

(def init
  (partial request "debot.init"))
(defn init!
  [& args]
  (-> (apply init args)
      doall
      last
      :params-json))

(def start
  (partial request "debot.start"))
(defn start!
  [& args]
  (-> (apply start args)
      doall
      last
      :params-json))

(def fetch
  (partial request "debot.fetch"))
(defn fetch!
  [& args]
  (-> (apply fetch args)
      doall
      last
      :params-json))

(def execute
  (partial request "debot.execute"))
(defn execute!
  [& args]
  (-> (apply execute args)
      doall
      last
      :params-json))

(def send
  (partial request "debot.send"))
(defn send!
  [& args]
  (-> (apply send args)
      doall
      last
      :params-json))

(def remove
  (partial request "debot.remove"))
(defn remove!
  [& args]
  (-> (apply remove args)
      doall
      last
      :params-json))

