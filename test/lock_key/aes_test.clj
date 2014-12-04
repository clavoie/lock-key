(ns lock-key.aes-test
  (:require
    [lock-key.aes :refer :all]
    [clojure.test :refer :all])
  (:refer-clojure :exclude [key]))

(def key "test key")
(def value "test value")

(deftest test-encrypt
  (is (not (= value
              (String. (encrypt value key))))))

(deftest test-decrypt
  (is (= value
         (String. (decrypt (encrypt value key) key)))))


