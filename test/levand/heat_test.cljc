(ns levand.heat-test
  (:require [net.cgrand.enlive-html :as e]
            [levand.heat :as h]
            [levand.heat.hiccup-spec :as hs]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [com.gfredericks.test.chuck.generators :as gen']
            [clojure.test.check.generators :as tcgen]
            [clojure.test.check.clojure-test :as tc]
            [clojure.test.check.properties :as prop]
             #?(:clj [clojure.test :refer [deftest is are run-tests]]
               :cljs [cljs.test :refer-macros [deftest is are run-tests]])))

(s/def ::hs/tag (s/with-gen ::hs/tag
                  #(gen/fmap keyword (gen'/string-from-regex hs/tag-re))))

;; TODO: These are ugly, because we need to repeat the specs instead of "modifying" existing ones
(s/def ::hs/attrs (s/with-gen ::hs/attrs
                    #(s/gen (s/map-of simple-keyword? any? :min-count 1))))

;; TODO: Gen of vector regular expressions seems like it should be built in?
(s/def ::hs/element (s/with-gen ::hs/element
                      #(gen/fmap vector (s/gen (s/cat :tag ::hs/tag
                                                      :attrs (s/? ::hs/attrs)
                                                      :children (s/* ::hs/node))))))

;; TODO: Implement this. Running into multiple bugs(?) with generation
#_(tc/defspec hiccup-data-round-trips 2
  (prop/for-all [hiccup (s/gen ::hs/element)]
    (= hiccup (h/hiccup (first (e/html hiccup))))))

(def template [:div#foo
               [:div.container
                [:span "foo"]]])

(def output [:div.container
             [:span "bar"]])


(deftest transform-test
  (is (= output (h/transform template [:.container]
                  [:span] (e/content "bar")))))

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