(ns onyx-starter.models.candidate
  (:require
   [onyx-starter.models.utils :as utils]
   [onyx-starter.models.stage :as stage]
   [onyx-starter.models.posting :as posting]
   [onyx-starter.models.candidate :as candidate]
   [datomic.api :only (q db) :as d]))

(defn make [x]
  {:candidate/name (:name x)})

(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :candidate/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db.install/_attribute :db.part/db}])

(def fixture
  [{:name "CDung"}
   {:name "CHung"}
   {:name "CAnh"}])

(defn create-random [conn name]
  (let [posting-eid (utils/random-eid conn
                                      :posting/name
                                      posting/fixture)
        candidate-eid (utils/random-eid conn
                                        :candidate/name
                                        candidate/fixture)
        stage-eid (utils/random-eid conn
                                    :stage/name
                                    stage/fixture)
        id (utils/get-eid
            (d/transact conn [{:db/id (d/tempid :db.part/user)
                               :candidate/name name}]))]

    ;; create posting-candidate
    (d/transact conn
                [{:db/id (d/tempid :db.part/user)
                  :posting-candidate/hired (rand-nth [true false])
                  :posting-candidate/referred (rand-nth [true false])
                  :posting-candidate/posting #{posting-eid}
                  :posting-candidate/candidate #{id}
                  :posting-candidate/stage #{stage-eid}}])))
