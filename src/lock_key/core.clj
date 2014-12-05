(ns lock-key.core
  "Wrapper around the Java cryptography extensions in javax.crypto.

   Implements symmetric encryption in AES/CBC/PKCS5Padding mode.
   "
  (:require
   [charset.bytes :refer [utf8-bytes]]
   [lock-key.private.core :as private])
  (:import
    [javax.crypto Cipher]
    [java.security SecureRandom]))

(defn encrypt
  "Symmetrically encrypts value with encryption-key, such that it can be
  decrypted later with (decrypt). Returns byte[]. The first 16 bytes of
  the returned value are the initialization vector. The remainder is the encrypted data.

  value           - (String/byte[]) the value to encrypt. Throws IllegalArgumentException
                    if value is an invalid type.
  encryption-key  - (String) the key with which to encrypt value with. Throws
                    IllegalArgumentException if encryption-key is empty"
  ^bytes
  [value ^String encryption-key]
  (if (not (or (string? value)
               (private/byte-array? value)))
    (throw (IllegalArgumentException. "Argument [value] must be of type String or byte[]")))
  (if (empty? encryption-key)
    (throw (IllegalArgumentException. "Argument [encryption-key] must not be empty")))
  (let [value-bytes (if (string? value)
                (utf8-bytes value)
                value)
        sr (SecureRandom/getInstance "SHA1PRNG")

        ;; Create a random initialization vector:
        iv-bytes (byte-array 16)
        _ (.nextBytes sr iv-bytes)

        cipher (private/get-cipher Cipher/ENCRYPT_MODE encryption-key iv-bytes)]
    (into-array Byte/TYPE (concat iv-bytes
                                  (.doFinal cipher value-bytes)))))

(defn decrypt
  "Decrypts a value which has been encrypted via a call to
   (encrypt) using key. The value parameter is expected to be a
   byte[] and the key parameter is expected to be a String.
   Returns byte[].

   The first 16 bytes of the input value is the initialization vector
   to use when decrypting. The remainder is the encrypted data."
  ^bytes
  [^bytes value ^String encryption-key]
  (let [[iv-bytes encrypted-data] (split-at 16 value)
        iv-bytes       (into-array Byte/TYPE iv-bytes)
        encrypted-data (into-array Byte/TYPE encrypted-data)
        cipher         (private/get-cipher Cipher/DECRYPT_MODE encryption-key iv-bytes)]
    (.doFinal cipher encrypted-data)))

(defn decrypt-as-str
  ""
  [value encryption-key]
  (String. (decrypt value encryption-key)))

