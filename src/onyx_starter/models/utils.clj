(ns onyx-starter.models.utils
  (:require
   [datomic.api :only (q db) :as d]
   [onyx-starter.models.user :as user]))

(defn data-with-dbid
  [data]
  (map #(merge {:db/id
                (d/tempid :db.part/user)} %1) data))

(defn make-fixture
  ([make fixtures fn]
   (into []
         (flatten
          (map fn
               (map data-with-dbid
                    (map make fixtures))))))
  ([make fixtures]
   (into []
         (flatten
          (map data-with-dbid
               (map make fixtures))))))

(defn random-name [xs]
  (rand-nth
   (map #(:name %) xs)))


(defn by-name [conn key name]
  (ffirst
   (d/q '[:find ?e
          :in $ ?name ?key
          :where [?e ?key ?name]]
        (d/db conn)
        name
        key)))

(defn get-eid [tx]
  (:e (second (:tx-data @tx))))

(defn random-eid [conn key fixtures]
  (by-name conn key (random-name fixtures)))

(comment
  (prn
   (make-fixture user/make user/fixture))
  )
