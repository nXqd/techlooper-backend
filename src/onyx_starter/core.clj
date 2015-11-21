(ns onyx-starter.core
  (:require
   [clj-kafka.zk :as zk]
   [onyx-starter.schema :refer [schema schema-analytics]]
   [onyx-starter.models.utils :as utils]
   [onyx-starter.models.user :as user]
   [onyx-starter.models.hr-view :as hr-view]
   [onyx-starter.models.stage :as stage]
   [onyx-starter.models.candidate :as candidate]
   [onyx-starter.models.posting :as posting]
   [onyx-starter.models.candidate-source :as candidate-source]
   [onyx-starter.models.utils :refer [make-fixture]]
   [clojure.data.json :as json]
   [datomic.api :as d]
   [clojure.core.async :as async]
   [onyx.plugin.core-async :refer [take-segments!]]
   [com.stuartsierra.component :as component]
   [taoensso.timbre :refer [info error] :as timbre]
   [onyx.plugin.kafka]
   [clj-kafka.producer :as kp]
   [clj-kafka.admin :as kadmin]
   [onyx.kafka.embedded-server :as ke]
   [onyx.api]
   [midje.sweet :refer :all]))

(def id (java.util.UUID/randomUUID))

(def zk-addr "127.0.0.1:2182")

(def analytics-uri
  "datomic:free://localhost:4334/analytics")
(def db-uri "datomic:free://localhost:4334/app")

(d/delete-database db-uri)
(d/create-database db-uri)

(d/delete-database analytics-uri)
(d/create-database analytics-uri)

(def conn (d/connect db-uri))
(def db (d/db conn))

(def conn-analytics (d/connect analytics-uri))
(def db-analytics (d/db conn-analytics))

;; load schema
(d/transact
 conn
 schema)

(d/transact
 conn-analytics
 schema-analytics)

;; load fixture
(d/transact
 conn
 (make-fixture user/make user/fixture))

(d/transact
 conn
 (make-fixture stage/make stage/fixture))

(d/transact
 conn
 (make-fixture candidate/make candidate/fixture))

(d/transact
 conn
 (make-fixture candidate-source/make candidate-source/fixture))


(defn data-with-user-eid
  [data]
  (map #(merge
         {:posting/user #{(user/random-eid conn)}}
         %) data))


(d/transact
 conn
 (make-fixture posting/make posting/fixture data-with-user-eid))

(def users
  (d/q '[:find [(pull ?e [:db/id
                          :user/name]) ...]
         :where [?e :user/name]]
       (d/db conn)))

(def postings
  (d/q '[:find [(pull ?e [:db/id
                          :posting/user
                          :posting/name]) ...]
         :where [?e :posting/name]]
       (d/db conn)))

(def candidate-sources
  (d/q '[:find [(pull ?e [:db/id
                          :candidate-source/name]) ...]
         :where [?e :candidate-source/name]]
       (d/db conn)))

;; Onyx
(def env-config
  {:zookeeper/address zk-addr
   :zookeeper/server? true
   :zookeeper.server/port 2182
   :onyx/id id})

(def peer-config
  {:zookeeper/address zk-addr
   :onyx.peer/job-scheduler :onyx.job-scheduler/greedy
   :onyx.messaging/impl :aeron
   :onyx.messaging/peer-port 40199
   :onyx.messaging/bind-addr "localhost"
   :onyx/id id})

(def env (onyx.api/start-env
          env-config))

(def peer-group (onyx.api/start-peer-group peer-config))

(with-open [zk (kadmin/zk-client zk-addr)]
  (kadmin/create-topic zk "test"
                       {:partitions 2}))


(defn deserialize-message [bytes]
  (read-string (String. bytes "UTF-8")))

;; herrrr

;; nhan vao candidate (name
;; create candidate
;; posting-candidate create random
;; aggregate
(defn candidate-transform [x]
  (let [name (:name x)]

    ;; insert
    (d/transact
     conn
     [{:db/id (d/tempid :db.part/user)
       :candidate/name name}])

    ;; create posting-candidate
    (candidate/create-random conn name)

    ;;

    ) x)

;; > name candidate
;; > posting-name
;; >

(candidate/create-random conn "Hung")

(defn get-all [eid]
  (d/pull (d/db conn) '[*] eid))

(defn create-hr-view [name]
  (let [candidate-eid (utils/by-name conn :candidate/name name)
        posting-candidate (d/q '[:find
                                 [(pull ?e [*])]
                                 :in $ ?candidate-eid
                                 :where
                                 [?e :posting-candidate/candidate ?candidate-eid]]
                               (d/db conn)
                               candidate-eid)
        posting (:db/id
                 (:posting-candidate/posting  (first posting-candidate)))
        hired (get-in (first posting-candidate)
                      [:posting-candidate/hired])
        referred (get-in (first posting-candidate)
                      [:posting-candidate/referred])
        user (get-in
              (d/pull (d/db conn) '[*] posting)
              [:posting/user :db/id])
        username (:user/name (get-all user))
        posting-name (:posting/name (get-all posting))
        hr-view (hr-view/get-by-names
                 conn-analytics
                 username
                 posting-name)
        hr-view-existed? (not (empty? hr-view))

        ;;count
        hired-count (if hr-view-existed?   0)
        applied-count (if hr-view-existed?   0)
        sourced-count (if hr-view-existed?   0)
        referred-count (if hr-view-existed?   0)
        all-count (+ hired-count
                     applied-count
                     sourced-count
                     referred-count)]

    (d/transact
     conn-analytics
     [{:db/id (d/tempid :db.part/user)
       :hr-view/posting-name posting-name
       :hr-view/hired hired
       :hr-view/hired hired
       :hr-view/username username}])
    ;; check if hr_view existed


    ;;
    (prn username)
    (prn posting-name)))

(def get-hired-count [existed? hr-view hired?]

  )

;; please move to hr_view

(get-all 17592186045418)
(ffirst
 (hr-view/get-by-names
  conn-analytics
  "QA" "Backend developer")
 )

(defn get-by-names
  [conn username posting-name]
  (d/q '[:find ?e .
         :in $ ?username ?posting-name
         :where
         (and [?e :hr-view/username ?username]
              [?e :hr-view/posting-name ?posting-name])]
       (d/db conn)
       username
       posting-name))

(get-by-names
  conn-analytics
  "QA" "Backend developer")


;; example data
(d/transact
 conn-analytics
 [{:db/id (d/tempid :db.part/user)
   :hr-view/posting-name "Backend developer"
   :hr-view/username "QA"}])

(candidate-transform {:name "test object"})

(def workflow
  [[:read-messages :candidate-transform]
   [:candidate-transform :out]])

#_(def workflow
  [[:read-messages :identity]
   [:identity :out]])

(def catalog
  [{:onyx/name :read-messages
    :onyx/plugin :onyx.plugin.kafka/read-messages
    :onyx/type :input
    :onyx/medium :kafka
    :kafka/topic "test"
    :kafka/group-id "onyx-consumer"
    :kafka/fetch-size 307200
    :kafka/chan-capacity 1000
    :kafka/zookeeper  "127.0.0.1:2181"
    :kafka/offset-reset :smallest
    :kafka/force-reset? true
    :kafka/empty-read-back-off 500
    :kafka/commit-interval 500
    :kafka/deserializer-fn :onyx-starter.core/deserialize-message
    :onyx/min-peers 1
    :onyx/max-peers 1
    :onyx/batch-size 100
    :onyx/doc "Reads messages from a Kafka topic"}

   {:onyx/name :identity
    :onyx/fn :clojure.core/identity
    :onyx/type :function
    :onyx/batch-size 100}

   {:onyx/name :candidate-transform
    :onyx/fn :onyx-starter.core/candidate-transform
    :onyx/type :function
    :onyx/batch-size 100}

   {:onyx/name :out
    :onyx/plugin :onyx.plugin.core-async/output
    :onyx/type :output
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-size 100
    :onyx/doc "Writes segments to a core.async channel"}

   ])

;; buffer
(def out-chan (async/chan 1024))

(async/go-loop []
  (try
    (let [x (async/<! out-chan)]
      (println "value from go loop" x))
    (catch Exception e
      (prn e)))
  (recur))

(defn inject-out-ch [event lifecycle]
  {:core.async/chan out-chan})

(def out-calls
  {:lifecycle/before-task-start inject-out-ch})

(def lifecycles
  [;; kafka
   {:lifecycle/task :read-messages
    :lifecycle/calls :onyx.plugin.kafka/read-messages-calls}

   ;; out
   {:lifecycle/task :out
    :lifecycle/calls :onyx-starter.core/out-calls}
   {:lifecycle/task :out
    :lifecycle/calls :onyx.plugin.core-async/writer-calls}
   ])


(def v-peers (onyx.api/start-peers 4 peer-group))

;; discover
(def zk-discover
  (:host
   (first
    (zk/brokers {"zookeeper.connect" "127.0.0.1:2181"}))))

(str zk-discover ":9092")

;; run
(onyx.api/submit-job
 peer-config
 {:catalog catalog
  :workflow workflow
  :lifecycles lifecycles
  :task-scheduler :onyx.task-scheduler/balanced})

(def producer
  (kp/producer
   {"metadata.broker.list" (str zk-discover ":9092")
    "serializer.class" "kafka.serializer.DefaultEncoder"
    "partitioner.class" "kafka.producer.DefaultPartitioner"}))

(def candidate
  {:name "Hung chim queo"})

(kp/send-message producer
                 (kp/message "test" (.getBytes (pr-str candidate))))

;; functions

(defn stop []
  (doseq [v-peer v-peers]
    (onyx.api/shutdown-peer v-peer))
  (onyx.api/shutdown-peer-group peer-group)
  (onyx.api/shutdown-env env)
  (component/stop kafka-server))

(stop)
