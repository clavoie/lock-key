(ns lock-key.core
  (:require
    [charset-bytes.core :refer [utf8-bytes]])
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

(defn encrypt [value key]
  (let [bytes (utf8-bytes value)
        cipher (get-cipher Cipher/ENCRYPT_MODE key)]
    (.doFinal cipher bytes)))

(defn decrypt [value key]
  (let [cipher (get-cipher Cipher/DECRYPT_MODE key)]
    (.doFinal cipher value)))
