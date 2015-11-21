(ns onyx-starter.models.analytics.hr-view
  (:require
   [datomic.api :only (q db) :as d]))

(defn make [x]
  {:hr-view/username (:username x)
   :hr-view/posting-name (:posting-name x)
   :hr-view/hired (:hired x)
   :hr-view/applied (:applied x)
   :hr-view/sourced (:sourced x)
   :hr-view/referred (:referred x)
   :hr-view/all (:all x)})

(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :hr-view/username
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :hr-view/posting-name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :hr-view/hired
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :hr-view/applied
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :hr-view/sourced
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :hr-view/referred
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :hr-view/all
    :db/valueType :db.type/long
    :db.install/_attribute :db.part/db}]
  )

(def fixture)
