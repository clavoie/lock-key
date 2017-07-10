(defproject lock-key "1.5.0"
  :description "Symmetric encryption wrappers for Clojure"
  :url "https://github.com/clavoie/lock-key"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[charset "1.2.1"]
                 [org.clojure/clojure "1.8.0"]
                 [base64-clj "0.1.1"]]
  :global-vars {*warn-on-reflection* true}
  :plugins [[lein-codox "0.10.3"]]  
  :codox {:src-dir-uri "http://github.com/clavoie/lock-key/blob/master/"
          :src-linenum-anchor-prefix "L"})
