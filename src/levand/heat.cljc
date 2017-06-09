(ns levand.heat
  (:require [net.cgrand.enlive-html :as e]
            [clojure.string :as str]))

(defn- hiccup-name
  "Return the hiccup name for a node"
  [node]
  (let [class (-> node :attrs :class)
        id (-> node :attrs :id)]
    (keyword
      (str (name (:tag node))
           (when id (str "#" id))
           (when class (str "." (str/replace class \space \.)))))))

(defn- hiccup-node
  "Convert a single Enlive node to its Hiccup equivalent"
  [node]
  (if (map? node)
    (let [attrs (-> node :attrs (dissoc :id :class))
          name (hiccup-name node)]
      (into (if (empty? attrs) [name] [name attrs])
        (map hiccup-node (:content node))))
    node))

(defn hiccup
  "Given an Enlive data structure, return the equivalent Hiccup data structure."
  [node-or-nodes]
  (if (sequential? node-or-nodes)
    (map hiccup-node node-or-nodes)
    (hiccup-node node-or-nodes)))

(defmacro transform
  "Apply Enlive transformations to a Hiccup-style data structure.

   Takes three arguments:

   1. The template data structure (which should resolve to Hiccup-style data)
   2. An Enlive selector selecting a subset of the template.
   3. Any number of Enlive selector/transformation pairs.

   For example:

     (def template [:div#foo
                     [:div.container
                       [:span \"foo\"]]])

     (transform template [:#foo :.container]
        [:span] (e/content \"bar\"))


   Note that for performance reasons, the `template` expression is resolved
   once, and cached. Do not use this macro if the template is dynamic and may
   change between calls.

   Also note that it is possible for the template selector to match multiple
   nodes. In this case, only the first node will be transformed and returned."
  [template selector & transformations]
  (let [cache-symbol (gensym "template-cache__")]
    `(do
       (defonce ~cache-symbol (first (e/select (e/html ~template) ~selector)))
       (first (hiccup
                (e/at ~cache-symbol
                  ~@transformations))))))

(comment

  (def template [:div#foo
                 [:div.container
                  [:span "foo"]]])

  (transform template [:#foo :.container]
    [:span] (e/content "bar"))

  ;; =>

  (e/html [:div.container [:span "bar"]])

  (hiccup {:tag :div,
           :attrs {:class "container"},
           :content [{:tag :span, :attrs {}, :content ["bar"]}]})

  ;; =>

  [:div.container [:span "bar"]]



  )
