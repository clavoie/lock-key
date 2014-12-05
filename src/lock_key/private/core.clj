(ns lock-key.private.core
  "Private functions used by lock-key.core. Do not reference this namespace
  outside lock-key.core as these fns are subject to change."
  (:require
    [charset.bytes :refer [utf8-bytes]])
  (:import
    [javax.crypto Cipher KeyGenerator]
    [javax.crypto.spec SecretKeySpec IvParameterSpec]
    [java.security SecureRandom]))

(def byte-array-class (Class/forName "[B"))

(defn byte-array?
  "Tests if a value is a byte[]. Returns true/false

  object - the object to test"
  [object]
  (= byte-array-class (type object)))

(defn get-iv-bytes
  "Initializes and returns a random initialization vector byte[]"
  []
  (let [sr (SecureRandom/getInstance "SHA1PRNG")
        iv-bytes (byte-array 16)]
    (.nextBytes sr iv-bytes)
    iv-bytes))

(defn get-raw-key
  "Returns an AES SecretKey encoded as a byte[]

  seed - (String)the seed to initialize the key with"
  [seed]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr     (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr (utf8-bytes seed))
    (.init keygen 128 sr)
    (.. keygen generateKey getEncoded)))

(defn get-cipher
  "Returns an AES/CBC/PKCS5Padding Cipher which can be used to encrypt or decrypt a
  byte[], depending on the mode of the Cipher.

  mode     - (int) see https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html
                   for available modes. Typically this will be either Cipher/ENCRYPT_MODE
                   or Cipher/DECRYPT_MODE.
  seed     - (String) the encryption seed / secret
  iv-bytes - (byte[]) the initialization vector"
  ^Cipher
  [mode seed iv-bytes]
  (let [key-spec (SecretKeySpec. (get-raw-key seed) "AES")
        iv-spec  (IvParameterSpec. iv-bytes)
        cipher   (Cipher/getInstance "AES/CBC/PKCS5Padding")]
    (.init cipher (int mode) key-spec iv-spec)
    cipher))
