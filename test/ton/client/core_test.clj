(ns ton.client.core-test
  (:require [clojure.test :refer :all]
            [ton.client.core :refer :all]))


(def config "{\"network\": {\"server_address\": \"https://net.ton.dev\"}}")

(deftest create-context-test
  (testing "context handle is returned"
    (let [context (create-context config)]
      (is (true? (> context 0)))
      (destroy-context context))))

(deftest request-test
  (testing "getting sdk version asynchronously"
    (let [context (create-context config)
          results (request context "client.version" "")]
      (doseq [x results]
        (-> x :params-json println))
      ; (println (doall results))
      (destroy-context context))))

