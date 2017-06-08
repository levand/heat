(defproject levand/heat "0.1.0-SNAPSHOT"
  :description "H.E.A.T. - Hiccup Enlive Adapter & Transforms"
  :url "http://github.com/levand/heat"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [enlive "1.2.0-SNAPSHOT"] ;; from cljs-support branch of https://github.com/levand/enlive
                 ]
  :profiles {:dev {:resource-paths ["test/resources"]
                   :dependencies [[org.clojure/test.check "0.9.0"]
                                  [com.gfredericks/test.chuck "0.2.7"]]}
             :test {:plugins [[lein-cljsbuild "1.1.6"]
                              [lein-shell "0.4.0"]]
                    :dependencies [[org.clojure/clojurescript "1.9.562"]
                                   [org.clojure/test.check "0.9.0"]
                                   [com.gfredericks/test.chuck "0.2.7"]]
                    :resource-paths ["test/resources"]
                    :aliases {"test-clj" ["run" "-m" "levand.heat-test"]
                              "test-cljs" ["do" ["clean"]
                                           ["cljsbuild" "once" "advanced"]
                                           ["shell" "phantomjs" "target/js/test-advanced.js"]]
                              "test-all" ["do" ["test-clj"]
                                               ["test-cljs"]]}
                    :cljsbuild {:builds {:whitespace {:source-paths ["src" "test"]
                                                      :compiler {:output-to "target/js/test-ws.js"
                                                                 :optimizations :whitespace}}
                                         :advanced {:source-paths ["src" "test"]
                                                    :compiler {:output-to "target/js/test-advanced.js"
                                                               :externs ["resources/phantomjs-externs.js"]
                                                               :optimizations :advanced}}}}}}
  )
