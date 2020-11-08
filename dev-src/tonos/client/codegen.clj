(ns tonos.client.codegen
  (:require
    [clojure.java.io :refer [file]]
    [clojure.data.json :as json]
    [clojure.set :refer [rename-keys]]
    [clojure.string :as string]
    [cljstache.core :refer [render]]))

(def api (json/read-str (slurp "/tmp/api.json") :key-fn keyword))

(def ^:const module-template
"
(ns tonos.client.{{module-name}}
  (:require [tonos.client.core :refer [request]]))

{{#functions}}
(def {{fn-decorated-name}}
  (partial request \"{{module-name}}.{{fn-name}}\"))
{{#short-form-eligible}}
(defn {{fn-decorated-name}}!
  [& args]
  (-> (apply {{fn-decorated-name}} args)
      doall
      last
      :params-json))
{{/short-form-eligible}}

{{/functions}}
")

(defn- is-short-form-eligible
  [f]
  (let [black-list #{"subscribe_collection"
                     "send_message"
                     "wait_for_transaction"}]
    (not (contains? black-list f))))

(defn- enrich-module-data
  [m]
  (letfn [(enrich-fn-data [f]
            (-> f (assoc :short-form-eligible (is-short-form-eligible (:name f)))
                  (assoc :fn-decorated-name (string/replace (:name f) "_" "-"))
                  (rename-keys {:name :fn-name})))]
    (-> m (assoc :functions (map enrich-fn-data (:functions m)))
          (rename-keys {:name :module-name}))))

(defn generate
  []
  (doseq [m (:modules api)]
    (doto (file "target" "generated" "tonos" "client" (str (:name m) ".clj"))
      (-> .getParentFile .mkdirs)
      (spit (->> m enrich-module-data (render module-template))))))

(defn -main
  [& args]
  (generate))

