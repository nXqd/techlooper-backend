(ns onyx-starter.models.analytics.schema
  (:require
   [onyx-starter.models.analytics.hr-view :as hr-view]))

(def schema
  (into [] (concat
            hr-view/schema)))
