(ns tonos.client.client-test
  (:require
    [clojure.test :refer :all]
    [tonos.client.test-tools :as tt]
    [tonos.client.client :as client]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)

(defn context-fixture
  [f]
  (binding [*context* (core/create-context tt/dev-net-config)]
    (f)
    (core/destroy-context *context*)))

(use-fixtures :each context-fixture)

(deftest version-test
  (testing "getting client version"
    (is (-> (client/version! *context*)
            :version
            (= "1.0.0")))))

(deftest api-reference-test
  (testing "getting api reference"
    (is (-> (client/get-api-reference! *context*)
            :api
            nil?
            false?))))

