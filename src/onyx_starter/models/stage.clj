(ns onyx-starter.models.stage
  (:require
   [datomic.api :only (q db) :as d]))

(defn make [x]
  {:stage/name (:name x)})

(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :stage/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db.install/_attribute :db.part/db}])

(def fixture
  [{:name "New Applicant"}
   {:name "New Lead"}
   {:name "Recruiter Screen"}
   {:name "Phone Interview"}
   {:name "Onsite Interview"}
   {:name "Offer"}
   {:name "Archive"}])

;;

(defn random-name []
  (rand-nth
   (map #(:name %) fixture)))

(comment
  (def stage (random-name)))

(defn by-name [conn name]
  (ffirst
   (d/q '[:find ?e
          :in $ ?name
          :where [?e :stage/name ?name]]
        (d/db conn)
        name)))

(defn random-eid [conn]
  (by-name conn (random-name)))

(comment
  (def db-uri "datomic:free://localhost:4334/app")
  (def conn (d/connect db-uri))
  (def stage )
  )
