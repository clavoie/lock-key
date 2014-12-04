(ns lock-key.aes
  "Legacy lock-key implementation for v1.0.0 of lock-key. Keeping this around
  in case anyone needs to decrypt something using a previous version of lock-key,
  but will remove it in a future release."
  (:require
    [charset.bytes :refer [utf8-bytes]])
  (:import
    [javax.crypto Cipher KeyGenerator SecretKey]
    [javax.crypto.spec SecretKeySpec]
    [java.security SecureRandom]))

(defn- get-raw-key [seed]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr (utf8-bytes seed))
    (.init keygen 128 sr)
    (.. keygen generateKey getEncoded)))

(defn- get-cipher [mode seed]
  (let [key-spec (SecretKeySpec. (get-raw-key seed) "AES")
        cipher (Cipher/getInstance "AES")]
    (.init cipher mode key-spec)
    cipher))

(defn encrypt
  "Symmetrically encrypts value with key, such that it can be
   decrypted later with (decrypt). The value and key parameters are
   expected to be a String. Returns byte[]"
  [value key]
  (let [bytes (utf8-bytes value)
        cipher (get-cipher Cipher/ENCRYPT_MODE key)]
    (.doFinal cipher bytes)))

(defn decrypt
  "Decrypts a value which has been encrypted via a call to
   (encrypt) using key. The value parameter is expected to be a
   byte[] and the key parameter is expected to be a String.
   Returns byte[]"
  [value key]
  (let [cipher (get-cipher Cipher/DECRYPT_MODE key)]
    (.doFinal cipher value)))
