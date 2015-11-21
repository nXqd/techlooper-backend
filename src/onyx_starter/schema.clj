(ns onyx-starter.schema
  (:require
   [onyx-starter.models.hr-view :as hr-view]
   [onyx-starter.models.user :as user]
   [onyx-starter.models.stage :as stage]
   [onyx-starter.models.posting-candidate :as posting-candidate]
   [onyx-starter.models.candidate :as candidate]
   [onyx-starter.models.posting :as posting]
   [onyx-starter.models.candidate-source :as candidate-source]
   [datomic.api :only (q db) :as d]))

(def schema
  (into [] (concat
            user/schema
            stage/schema
            posting-candidate/schema
            posting/schema
            candidate/schema
            candidate-source/schema)))

(def schema-analytics
  (into [] (concat
            hr-view/schema)))
