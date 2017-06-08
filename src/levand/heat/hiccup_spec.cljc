(ns levand.heat.hiccup-spec
  "Specs for Hiccup-shaped data"
  (:require [clojure.spec.alpha :as s]))

;; Todo: extend tag to match hiccup tags with IDs ids and classes
(def tag-re #"[a-zA-Z][a-zA-Z0-9]*")

(s/def ::node (s/or :element ::element
                    :text string?))

(s/def ::tag (s/and simple-keyword? #(re-matches tag-re (name %))))

(s/def ::attrs (s/map-of simple-keyword? any?))

(s/def ::element
  (s/and vector?
    (s/cat :tag ::tag
      :attrs (s/? ::attrs)
      :children (s/* ::node))))

(comment

  (s/conform ::node [:foo {:class "foo"} "biz"])

  (s/assert ::node [:foo.bar {:class "foo"} "biz"])

  (s/check-asserts true)

  (s/conform ::tag :foo.bar)
  (s/assert ::tag :foo.bar)



  )
