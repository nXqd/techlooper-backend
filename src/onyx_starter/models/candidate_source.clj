(ns onyx-starter.models.candidate-source
  (:require
   [datomic.api :only (q db) :as d]))

(defn make [x]
  {:candidate-source/name (:name x)})

(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :candidate-source/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db.install/_attribute :db.part/db}])

(def fixture
  [{:name "linkedin"}
   {:name "itviec"}
   {:name "github"}
   {:name "vietnamwork"}
   {:name "careerlink"}
   {:name "careerbuilder"}
   {:name "jobstreet"}
   {:name "indeed"}])


;; utils
(defn by-name [conn name]
  (d/q '[:find ?e
         :in $ ?name
         :where [?e :candidate-source/name ?name]]
       (d/db conn)
       name))
