#!/usr/bin/env bb

(require '[babashka.pods :as pods])
(pods/load-pod 'org.babashka/buddy "0.0.1")

(pods/load-pod "./pod-babashka-buddy")

(require '[clojure.string :as str]
         '[pod.babashka.buddy.codecs :as codecs]
         '[pod.babashka.buddy.mac :as mac]
         '[pod.babashka.buddy.nonce :as nonce])

(def hash-algorithm :hmac+sha256)
(def secret (nonce/random-bytes 64))

(let [timestamp (System/currentTimeMillis)
      nonce (nonce/random-bytes 64)
      nonce-hex (codecs/bytes->hex nonce)
      payload (pr-str {:nonce nonce-hex :timestamp timestamp})
      signature (codecs/bytes->hex (mac/hash payload {:alg hash-algorithm :key secret}))]
  (prn (str/join "-" [nonce-hex timestamp signature])))