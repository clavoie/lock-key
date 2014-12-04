(ns lock-key.private.core-test
  (:require [lock-key.private.core :refer :all]
            [clojure.test :refer :all])
  (:import [javax.crypto Cipher]
           [java.security SecureRandom]))

(def byte-array-class (Class/forName "[B"))

(defn- byte-array?
  [object]
  (= byte-array-class (type object)))

(deftest get-raw-key-test
  (testing "no-empty secret key generated"
    (let [raw-key (get-raw-key "secret")]
      (is (byte-array? raw-key))
      (is (not (empty? raw-key)))))
  (testing "throws if secret is not string"
    (is (thrown? Exception (get-raw-key 1)))))

(deftest get-cipher-test
  (let [sr       (SecureRandom/getInstance "SHA1PRNG")
        iv-bytes (byte-array 16)
        _        (.nextBytes sr iv-bytes)]
    (testing "cipher type returned"
      (let [cipher   (get-cipher Cipher/ENCRYPT_MODE "secret" iv-bytes)]
        (is (instance? Cipher cipher))
        (is (= (seq iv-bytes) (seq (.getIV cipher))))))
    (testing "invalid mode throws exception"
      (is (thrown? Exception (get-cipher "mode" "secret" iv-bytes))))
    (testing "invalid seed throws exception"
      (is (thrown? Exception (get-cipher Cipher/ENCRYPT_MODE 1 iv-bytes))))
    (testing "invalid initialization vector throws exception"
      (is (thrown? Exception (get-cipher Cipher/ENCRYPT_MODE "secret" 1)))
      (is (thrown? Exception (get-cipher Cipher/ENCRYPT_MODE "secret" (byte-array 3)))))))
