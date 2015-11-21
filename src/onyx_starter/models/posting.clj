(ns onyx-starter.models.posting
  (:require
   [onyx-starter.models.user :as user]
   [datomic.api :only (q db) :as d]))


(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :posting/name
    :db/valueType :db.type/string
    :db/unique :db.unique/value
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   ;; relationship
   {:db/id #db/id[:db.part/db]
    :db/ident :posting/company
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :posting/status
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :posting/price
    :db/valueType :db.type/double
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :posting/import-tags
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/many
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :posting/interview-process
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   ;; relationship
   {:db/id #db/id[:db.part/db]
    :db/ident :posting/user
    :db/isComponent true
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   ;; relationship
   {:db/id #db/id[:db.part/db]
    :db/ident :posting/source
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}])

(defn make [x]
  {:posting/name (:name x)
   :posting/status (:status x)
   :posting/price (:price x)})

(def fixture
  [{:name "Backend Engineer"
    :status true
    :price 50.00}])

(comment
  (def db-uri "datomic:free://localhost:4334/app")
  (def conn (d/connect db-uri)))
