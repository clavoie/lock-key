(ns lock-key.core-test
  (:require
   [lock-key.core :refer :all]
   [lock-key.private.core :as private]
   [clojure.test :refer :all]))

(def secret "test key")
(def value "test value = 10€, süß")

(deftest test-encrypt
  (is (not= value  (String. (encrypt value secret))))
  (is (private/byte-array?  (encrypt value secret)))
  (is (thrown? Exception (encrypt :invalid secret)))
  (is (thrown? Exception (encrypt value :invalid)))
  (is (thrown? Exception (encrypt value ""))))

(deftest test-decrypt
  ;; FIXME: this will fail with default charset other than utf-8:
  (is (= value (String. (decrypt (encrypt value secret) secret))))
  (is (private/byte-array? (decrypt (encrypt value secret) secret)))
  (is (thrown? Exception (decrypt :invalid secret)))
  (is (thrown? Exception (decrypt (encrypt value secret) :invalid))))

(deftest test-decrypt-as-str
  (is (= value (decrypt-as-str (encrypt value secret) secret)))
  (is (thrown? Exception (decrypt-as-str :invalid secret)))
  (is (thrown? Exception (decrypt-as-str (encrypt value secret) :invalid))))

(deftest test-encrypt-as-base64
  (is (not= value  (encrypt-as-base64 value secret)))
  (is (string?  (encrypt-as-base64 value secret)))
  (is (thrown? Exception (encrypt-as-base64 :invalid secret)))
  (is (thrown? Exception (encrypt-as-base64 value :invalid)))
  (is (thrown? Exception (encrypt-as-base64 value ""))))

(deftest test-decrypt-from-base64
  ;; This could fail in 1.4.1 with the default charset other than
  ;; utf-8:
  (is (= value (decrypt-from-base64 (encrypt-as-base64 value secret) secret)))
  (is (thrown? Exception (decrypt-from-base64 :invalid secret)))
  (is (thrown? Exception (decrypt-from-base64 (encrypt-as-base64 value secret) :invalid))))

