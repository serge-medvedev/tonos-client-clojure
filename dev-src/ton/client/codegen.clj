(ns ton.client.codegen
  (:require
    [clojure.java.io :refer [file]]
    [clojure.data.json :as json]
    [clojure.set :refer [rename-keys]]
    [clojure.string :as string]
    [cljstache.core :refer [render]]))

(def api (json/read-str (slurp "/tmp/api.json") :key-fn keyword))

(def ^:const module-template
"
(ns ton.client.{{module-name}}
  (:require [ton.client.core :refer [request]]))

{{#functions}}
(def {{fn-decorated-name}}
  (partial request \"{{module-name}}.{{fn-name}}\"))

{{/functions}}
")

(defn- enrich-module-data
  [m]
  (letfn [(enrich-fn-data [f]
            (-> f (assoc :fn-decorated-name (string/replace (:name f) "_" "-"))
                  (rename-keys {:name :fn-name})))]
    (-> m (assoc :functions (map enrich-fn-data (:functions m)))
          (rename-keys {:name :module-name}))))

(defn generate
  []
  (doseq [m (:modules api)]
    (doto (file "target" "generated" "ton" "client" (str (:name m) ".clj"))
      (-> .getParentFile .mkdirs)
      (spit (->> m enrich-module-data (render module-template))))))

(defn -main
  [& args]
  (generate))

