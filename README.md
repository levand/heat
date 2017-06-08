# H.E.A.T - Hiccup-Enlive Adapter & Transforms

Provides tools to create Enlive templates from embedded Hiccup
data structures, rather than external HTML files.

Requires the ClojureScript-compatible refactoring of Enlive, which may
be found on the `cljs-support` branch of
 https://github.com/levand/enlive.

## Usage

The `levand.heat/transform` macro is the primary API, and provides a
succinct way to transform hiccup data into other hiccup data using
 Enlive transformations.

 It does so by wrapping the
  `net.cgrand.enlive/at` macro, along with the Hiccup->Enlive and
  Enlive->Hiccup data transformations that are required.

```clojure
(def template [:div#foo
                [:div.container
                  [:span "a node"]]])

(transform template [:#foo]
  [:span] (content "new content"))
```

Enlive has a built in function, `net.cgrand.enlive/html` to convert from
 Hiccup-shaped data structures to Enlive-shaped data.

This library provides a function to perform the inverse operation,
transforming Enlive data to the Hiccup shape: `levand.heat/hiccup`.

```clojure

(hiccup {:tag :div,
         :attrs {:id "foo"},
         :content [{:tag :div,
                    :attrs {:class "container"},
                    :content [{:tag :span, :attrs {}, :content ["a node"]}]}]})

;; yeilds:

[:div#foo [:div.container [:span "a node"]]]
```

## License

Copyright © 2017 Luke VanderHart

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
