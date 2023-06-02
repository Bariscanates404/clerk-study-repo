(ns ns-sharing-problem-1)

(require '[datomic.client.api :as d])
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))
(d/create-database client {:db-name "db01"})
(def conn (d/connect client {:db-name "db01"}))
(def db (d/db conn))                                        ;;refresh database

(def db-schema
  [{:db/ident       :item/name
    :db/valueType   :db.type/string
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :item/sum
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data db-schema})
(def db (d/db conn))                                        ;;refresh database

