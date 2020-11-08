(ns tonos.client.utils-test
  (:require
    [clojure.test :refer :all]
    [tonos.client.test-tools :as tt]
    [tonos.client.utils :as utils]
    [tonos.client.core :as core]))


(def ^:dynamic *context* nil)

(defn- tf
  [f]
  (binding [*context* (core/create-context tt/main-net-config)]
    (f)
    (core/destroy-context *context*)))

(use-fixtures :once tf)

(deftest convert-address-test
  (testing "converting the address"
    (let [params {:address "0:b453e53ae4ae0d8104592c1127298aecb637bb70a0bcd56322cf7731a66ce1d2"
                  :output_format {:type "Base64"
                                  :url false
                                  :test false
                                  :bounce true}}]
      (is (-> (utils/convert-address! *context* params)
              :address
              (= "EQC0U+U65K4NgQRZLBEnKYrstje7cKC81WMiz3cxpmzh0sJJ"))))))

