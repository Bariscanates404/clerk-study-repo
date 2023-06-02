(ns T02b-analysis-by-symbols)

(require '[datomic.client.api :as d]
         '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         '[repo-analysis-app-resuables :as r]
         )
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))
(d/create-database client {:db-name "db01"})
(def conn (d/connect client {:db-name "db01"}))
(def db (d/db conn))                                        ;;refresh database
(def db-schema
  [{:db/ident       :function/name
    :db/valueType   :db.type/string
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :function/sum
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data db-schema})
(def db (d/db conn))


(def type-vec ["map" "reduce" "mapv" "reduve-kv" "juxt" "assoc"])

(defn add-a-type-into-schema "adds just one item in functions schema" [type-str]
  (d/transact conn {:tx-data [{:function/name type-str
                               :function/sum  0
                               }
                              ]})
  (def db (d/db conn))
  )

(defn create-type-schema "adds multiple items in functions schema"
  [type-vec]
  (doall (for [len (range 0 (count type-vec))]
           (add-a-type-into-schema (get type-vec len))
           )
         )
  )
(create-type-schema type-vec)
(def vector-0f-texts "gets all the project files and return them as map of strings"
  (mapv
    (fn [file] {:name (.getName file), :content (slurp file)})
    (filter
      (fn [file] (not (.isDirectory file)))
      (r/regex-file-seq #".*\.(clj[cs]?)$" (clojure.java.io/file "/Users/bariscanates/study/clj"))))
  )
(def !type "helps to run increase-usages function inside of main functions for loop"
  (atom ["String"])
  )
(defn f "searchs the given value(inside !type atom) inside given text(vector-0f-texts)"
  [text]
  (count (re-seq (re-pattern (str "\\Q" (get @!type 0) "\\E" "[^a-zA-Z0-9*+!\\-_'?]")) text)))
(defn function-usages "shows  the number of given functions usages which are saved db until now on"
  [func-name]
  (ffirst (d/q
            '[:find ?name
              :in $ ?func-name
              :where
              [?e :function/name ?func-name]
              [?e :function/sum ?name]]
            db func-name))
  )
(defn increase-usages "counts the usage sum of given function name and writes into the db the sum of old and new usage numbers"
  [func-name-string func-usage-to-sum]
  (def db (d/db conn))
  (d/transact conn {:tx-data [{:function/name func-name-string
                               :function/sum  (+ (function-usages func-name-string) func-usage-to-sum)}
                              ]})
  (def db (d/db conn))
  )
(defn main-function "checks all of given function(by function type vector) usages into given project and writes their sum into db"
  [type-coll]
  (doall (for [len (range 0 (count type-coll))]
           (do
             (reset! !type [(get type-vec len)])
             (increase-usages (get type-coll len) (reduce + (into [] (map f (map :content vector-0f-texts)))))
             )
           )
         )
  )
(main-function type-vec)
(def all-func-usages "returns all saved function usages into db"
  (reverse (->> (d/q
                  '[:find ?func-name ?sum
                    :where
                    [?e :function/name ?func-name]
                    [?e :function/sum ?sum]]
                  db)
                (sort-by last)
                )
           )
  )
(def result-page
  (into [] all-func-usages))
(r/create-cvs result-page)
(println (slurp "tmp/foo.csv"))