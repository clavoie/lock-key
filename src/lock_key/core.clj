(ns lock-key.core
  "Wrapper around the Java cryptography extensions in javax.crypto. Implements
  symmetric encryption in AES/CBC/PKCS5Padding mode."
  (:require
   [charset.bytes :refer [utf8-bytes]]
   [lock-key.private.core :as private]
   [base64-clj.core :as base64])
  (:import
    [javax.crypto Cipher]))

(defn encrypt
  "Symmetrically encrypts value with encryption-key, such that it can be
  decrypted later with (decrypt).

  Returns byte[]. The first 16 bytes of the returned value are the
  initialization vector. The remainder is the encrypted data.

  value           - (String/byte[]) the value to encrypt. Throws IllegalArgumentException
                    if value is an invalid type.
  encryption-key  - (String) the key with which to encrypt value with. Throws
                    IllegalArgumentException if encryption-key is empty"
  ^bytes
  [value encryption-key]

  (if (not (or (string? value)
               (private/byte-array? value)))
    (throw (IllegalArgumentException. "Argument [value] must be of type String or byte[]")))

  (if (empty? encryption-key)
    (throw (IllegalArgumentException. "Argument [encryption-key] must not be empty")))

  (let [value-bytes (if (string? value) (utf8-bytes value) value)
        iv-bytes (private/get-iv-bytes)
        cipher (private/get-cipher Cipher/ENCRYPT_MODE encryption-key iv-bytes)]
    (into-array Byte/TYPE (concat iv-bytes
                                  (.doFinal cipher value-bytes)))))

(defn decrypt
  "Decrypts a value which has been encrypted via (encrypt). Returns byte[].

  The first 16 bytes of the input value is the initialization vector to use when
  decrypting. The remainder is the encrypted data.

  value          - (byte[]) the value to decrypt
  encryption-key - (String) the key with which the value was encrypted"
  ^bytes
  [value encryption-key]
  (let [[iv-bytes encrypted-data] (split-at 16 value)
        iv-bytes       (into-array Byte/TYPE iv-bytes)
        encrypted-data (into-array Byte/TYPE encrypted-data)
        cipher         (private/get-cipher Cipher/DECRYPT_MODE encryption-key iv-bytes)]
    (.doFinal cipher encrypted-data)))

(defn decrypt-as-str
  "Decryptes a value which has been encrypted via (encrypt) and attempts to
  construct the decrypted value into a string.

  value          - (byte[]) the value to decrypt
  encryption-key - (String) the key with which the value was encrypted"
  [value encryption-key]
  (String. (decrypt value encryption-key)))

(defn encrypt-as-websafe
  "Same as encrypt, but returns a base64 encoded string.  This makes the results
  web friendly, so that you can use easily use them with things like JSON"
  [value encryption-key]
  (String. (base64/encode-bytes (encrypt value encryption-key))))

(defn decrypt-from-websafe
  "Same as decrypt, but accepts a base64 encoded string as input."
  [value encryption-key]
  (String. (decrypt (base64/decode-bytes (.getBytes value)) encryption-key)))
