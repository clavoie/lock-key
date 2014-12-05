(ns lock-key.core-test
  (:require
    [lock-key.core :refer :all]
    [clojure.test :refer :all])
  (:refer-clojure :exclude [key]))

(def key "test key")
(def value "test value")

(deftest test-encrypt
  (testing "value encrypted"
    (is (not (= value
                (String. (encrypt value key))))))
  (testing "invalid value"
    (is (thrown? Exception (encrypt 1 key))))
  (testing "invalid key"
    (is (thrown? Exception (encrypt value "")))))

(deftest test-decrypt
  (is (= value
         (String. (decrypt (encrypt value key) key)))))


