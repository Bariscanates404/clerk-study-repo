(ns ns-sharing-problem-2
  (:require [ns-sharing-problem-1 :as mp]))
  (require '[datomic.client.api :as d])

(d/transact mp/conn {:tx-data [{:item/name "String"
                             :item/sum  0}
                            ]})
(d/transact mp/conn {:tx-data [{:item/name    "String"
                             :item/sum 10}]})
(def db (d/db mp/conn))   ;;yada (mp/db)


(d/q
  '[:find ?sum
    :in $ ?name
    :where
    [?e :item/name ?name]
    [?e :item/sum ?sum]]
  db "String")