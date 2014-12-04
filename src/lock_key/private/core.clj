(ns lock-key.private.core
  "Private functions used by lock-key.core."
  (:require
    [charset.bytes :refer [utf8-bytes]])
  (:import
    [javax.crypto Cipher KeyGenerator]
    [javax.crypto.spec SecretKeySpec IvParameterSpec]
    [java.security SecureRandom]))

(defn get-raw-key [^String seed]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr     (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr ^bytes (utf8-bytes seed))
    (.init keygen 128 sr)
    (.. keygen generateKey getEncoded)))

(defn ^Cipher get-cipher
  [mode ^String seed ^bytes iv-bytes]
  (let [key-spec (SecretKeySpec. (get-raw-key seed) "AES")
        iv-spec  (IvParameterSpec. iv-bytes)
        cipher   (Cipher/getInstance "AES/CBC/PKCS5Padding")]
    (.init cipher (int mode) key-spec iv-spec)
    cipher))
