(ns onyx-starter.models.user
  (:require
   [datomic.api :only (q db) :as d]))

(defn make [x]
  {:user/name (:name x)
   :user/email (:email x)
   :user/role (:role x)
   :user/gender (:gender x)
   :user/title (:title x)})

(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :user/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :user/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :user/role
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :user/gender
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :user/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :user/location
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}

   ;; relationship
   {:db/id #db/id[:db.part/db]
    :db/ident :user/company
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db.install/_attribute :db.part/db}]
  )

(def fixture
  [{:name "QA"
    :email "qa@email.com"
    :role "hr"
    :gender "male"
    :title "Hiring Manager"}

   {:name "Dung"
    :email "dung@email.com"
    :role "hr"
    :gender "male"
    :title "Hiring Agency"}

   {:name "Hung"
    :email "hung@email.com"
    :role "hr"
    :gender "male"
    :title "Hiring Agency"}

   {:name "Trung"
    :email "trung@email.com"
    :role "team"
    :gender "male"
    :title "Hiring Agency"}

   {:name "Hao"
    :email "hao@email.com"
    :role "team"
    :gender "male"
    :title "Frontend Engineer"}

   {:name "Han"
    :email "han@email.com"
    :role "team"
    :gender "male"
    :title "Backend Engineer"}

   {:name "Trinh"
    :email "trinh@email.com"
    :role "team"
    :gender "male"
    :title "Sales Lead"}])

;;
(defn random-name []
  (rand-nth
   (map #(:name %)
        (filter (fn [x]
                  (= (:role x) "hr")) fixture))))

(comment
  (def user (random-name)))

(defn by-name [conn name]
  (ffirst
   (d/q '[:find ?e
          :in $ ?name
          :where [?e :user/name ?name]]
        (d/db conn)
        name)))

(defn random-eid [conn]
  (by-name conn (random-name)))

(comment
  (def db-uri "datomic:free://localhost:4334/app")
  (def conn (d/connect db-uri))
  (random-eid conn)
  )
