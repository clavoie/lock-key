(ns lock-key.private.core-test
  (:require [lock-key.private.core :refer :all]
            [clojure.test :refer :all])
  (:import [javax.crypto Cipher]))

(deftest byte-array?-test
  (is (byte-array? (byte-array 10)))
  (is (not (byte-array? nil)))
  (is (not (byte-array? (seq "hey")))))

(deftest get-iv-bytes-test
  (let [iv1 (get-iv-bytes)
        iv2 (get-iv-bytes)]
    (is (byte-array? iv1))
    (is (byte-array? iv2))
    (is (not (empty? iv1)))
    (is (not (empty? iv2)))
    (is (not= (seq iv1) (seq iv2)))))

(deftest get-raw-key-test
  (let [raw-key (get-raw-key "secret")]
    (is (byte-array? raw-key))
    (is (not (empty? raw-key))))
  (is (thrown? Exception (get-raw-key :invalid))))

(deftest get-cipher-test
  (let [iv-bytes (get-iv-bytes)]
    (let [cipher   (get-cipher Cipher/ENCRYPT_MODE "secret" iv-bytes)]
      (is (instance? Cipher cipher))
      (is (= (seq iv-bytes) (seq (.getIV cipher)))))
    (is (thrown? Exception (get-cipher "mode" "secret" iv-bytes)))
    (is (thrown? Exception (get-cipher Cipher/ENCRYPT_MODE :invalid iv-bytes)))
    (is (thrown? Exception (get-cipher Cipher/ENCRYPT_MODE "secret" :invalid)))
    (is (thrown? Exception (get-cipher Cipher/ENCRYPT_MODE "secret" (byte-array 3))))))
