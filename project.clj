(defproject onyx-starter "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.onyxplatform/onyx "0.8.0-alpha1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.onyxplatform/onyx-datomic "0.8.0.1"]
                 [com.datomic/datomic-free "0.9.5327"
                  :exclusions [joda-time]]
                 [zookeeper-clj "0.9.3"
                  :exclusions [io.netty/netty
                               org.apache.zookeeper/zookeeper]]
                 [org.onyxplatform/onyx-kafka "0.8.0.1"]
                 [com.stuartsierra/component "0.2.3"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [midje "1.7.0"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-autotest "0.1.0"]
                             [lein-set-version "0.4.1"]
                             [lein-update-dependency "0.1.2"]
                             [lein-pprint "1.1.1"]]
                   :source-paths ["env/dev" "src"]}})
