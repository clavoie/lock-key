(ns lock-key.core
  "Wrapper around the Java cryptography extensions in javax.crypto.

   Implements symmetric encryption in AES/CBC/PKCS5Padding mode.
   "
  (:require
    [charset.bytes :refer [utf8-bytes]])
  (:import
    [javax.crypto Cipher KeyGenerator SecretKey]
    [javax.crypto.spec SecretKeySpec IvParameterSpec]
    [java.security SecureRandom]))

(defn- get-raw-key [^String seed]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr     (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr ^bytes (utf8-bytes seed))
    (.init keygen 128 sr)
    (.. keygen generateKey getEncoded)))


(defn- ^Cipher get-cipher
  [mode ^String seed ^bytes iv-bytes]
  (let [key-spec (SecretKeySpec. (get-raw-key seed) "AES")
        iv-spec  (IvParameterSpec. iv-bytes)
        cipher   (Cipher/getInstance "AES/CBC/PKCS5Padding")]
    (.init cipher (int mode) key-spec iv-spec)
    cipher))


(defn encrypt
  "Symmetrically encrypts value with key, such that it can be
   decrypted later with (decrypt). The value may be either a
   UTF-8 String or a byte array. Key should be a String.

   Returns byte[]. The first 16 bytes of the returned value
   are the initialization vector. The remainder is the encrypted data."
  ^bytes [value ^String key]
  (assert (not (empty? key)))
  (let [bytes (if (string? value)
                ^bytes (utf8-bytes value)
                value)
        sr (SecureRandom/getInstance "SHA1PRNG")

        ;; Create a random initialization vector:
        iv-bytes (byte-array 16)
        _ (.nextBytes sr iv-bytes)

        cipher (get-cipher Cipher/ENCRYPT_MODE key iv-bytes)]
    (into-array Byte/TYPE (concat iv-bytes
                                  (.doFinal cipher bytes)))))


(defn decrypt
  "Decrypts a value which has been encrypted via a call to
   (encrypt) using key. The value parameter is expected to be a
   byte[] and the key parameter is expected to be a String.
   Returns byte[].

   The first 16 bytes of the input value is the initialization vector
   to use when decrypting. The remainder is the encrypted data."
  ^bytes [^bytes value ^String key]
  (let [[iv-bytes encrypted-data] (split-at 16 value)
        iv-bytes       (into-array Byte/TYPE iv-bytes)
        encrypted-data (into-array Byte/TYPE encrypted-data)
        cipher         (get-cipher Cipher/DECRYPT_MODE key iv-bytes)]
    (.doFinal cipher encrypted-data)))

(defn decrypt-as-str
  ""
  [value key]
  (String. (decrypt value key)))

