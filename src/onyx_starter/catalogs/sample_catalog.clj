(ns onyx-starter.catalogs.sample-catalog)

;;; Catalogs describe each task in a workflow. We use
;;; them for describing input and output sources, injecting parameters,
;;; and adjusting performance settings.

(defn build-catalog [batch-size batch-timeout]
  [{:onyx/name :kafka-read-messages
    :onyx/plugin :onyx.plugin.kafka/read-messages
    :onyx/type :input
    :onyx/medium :kafka
    :kafka/topic "test"
    :kafka/group-id "onyx-consumer"
    :kafka/fetch-size 307200
    :kafka/chan-capacity 1000
    :kafka/zookeeper "127.0.0.1:2181"
    :kafka/offset-reset :smallest
    :kafka/force-reset? true
    :kafka/empty-read-back-off 500
    :kafka/commit-interval 500
    :kafka/deserializer-fn :my.ns/deserializer-fn
    :onyx/min-peers 10
    :onyx/max-peers 10
    :onyx/batch-size 100
    :onyx/doc "Reads messages from a Kafka topic"}

   {:onyx/name :out
    :onyx/plugin :onyx.plugin.core-async/output
    :onyx/type :output
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size
    :onyx/doc "Writes segments to a core.async channel"}])


#_[{:onyx/name :in
    :onyx/plugin :onyx.plugin.core-async/input
    :onyx/type :input
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size
    :onyx/doc "Reads segments from a core.async channel"}



   {:onyx/name :split-by-spaces
    :onyx/fn :onyx-starter.functions.sample-functions/split-by-spaces
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :mixed-case
    :onyx/fn :onyx-starter.functions.sample-functions/mixed-case
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :loud
    :onyx/fn :onyx-starter.functions.sample-functions/loud
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :question
    :onyx/fn :onyx-starter.functions.sample-functions/question
    :onyx/type :function
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size}

   {:onyx/name :loud-output
    :onyx/plugin :onyx.plugin.core-async/output
    :onyx/type :output
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size
    :onyx/doc "Writes segments to a core.async channel"}

   {:onyx/name :question-output
    :onyx/plugin :onyx.plugin.core-async/output
    :onyx/type :output
    :onyx/medium :core.async
    :onyx/max-peers 1
    :onyx/batch-timeout batch-timeout
    :onyx/batch-size batch-size
    :onyx/doc "Writes segments to a core.async channel"}]
