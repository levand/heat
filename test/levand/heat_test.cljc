(ns levand.heat-test
  (:require [net.cgrand.enlive-html :as e]
            [levand.heat :as h]
            [clojure.string :as str]
            #?(:clj [clojure.test :refer [deftest is are]]
               :cljs [cljs.test :refer-macros [deftest is are run-all-tests]])))

(deftest smoke
  (is (= 9.0 (h/foobar 3))))




#?(:cljs (enable-console-print!))

(defn -main
  "Entry point for running tests (until *.cljc tools catch up)"
  []
  #?(:clj
     (clojure.test/run-tests 'levand.heat-test)
     :cljs
     (cljs.test/run-tests 'levand.heat-test)))

;; Run tests at the root level, in CLJS
#?(:cljs
   (do (-main)
       (.exit js/phantom)))