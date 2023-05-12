(ns T01
  (:require [nextjournal.clerk :as clerk]))

(require '[datomic.client.api :as d])

(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))
(d/create-database client {:db-name "db01"})
(def conn (d/connect client {:db-name "db01"}))
(def db (d/db conn))                                        ;;refresh database

(def db-schema
  [{:db/ident       :product/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :product/label
    :db/valueType   :db.type/string
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :product/type
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :product/model-no
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data db-schema})

(def stock-schema
  [{:db/ident       :stock/product
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :order/stock
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data stock-schema})

(def order-schema
  [{:db/ident       :order/product
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :order/user
    :db/valueType   :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident       :order/size
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data order-schema})

(def user-schema
  [{:db/ident       :user/id
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :user/password
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data user-schema})

(def product-data
  [{:product/id       100
    :product/label    "lacoste"
    :product/type     "urban clothing"
    :product/model-no "polo shirt"}
   {:product/id       101
    :product/label    "canada goose"
    :product/type     "jackets"
    :product/model-no "caban"}
   {:product/id       102
    :product/label    "mammut"
    :product/type     "boots"
    :product/model-no "hiking boots"}
   {:product/id       103
    :product/label    "husky"
    :product/type     "sleeping bags"
    :product/model-no "arnapurna"}])
(d/transact conn {:tx-data product-data})


(def db (d/db conn))                                        ;;refresh database
(def show-all-products (for [[v] (d/q
                                   '[:find ?name
                                     :where
                                     [?e :product/label ?name]]
                                   db)]
                         (clerk/html [:table
                                      [:tr [:td v]]
                                      ])
                         )
  )

(def user-data
  [{:user/id       1
    :user/name     "admin01"
    :user/password "123456"}
   {:user/id       2
    :user/name     "userdemo"
    :user/password "123456"}
   ])
(d/transact conn {:tx-data user-data})


(defn get-entity-id-by-label [label]
  (ffirst (d/q
            '[:find ?e
              :in $ ?label
              :where
              [?e :product/label ?label]]
            db label)))
(def db (d/db conn))
(get-entity-id-by-label "lacoste")

(defn get-label-by-entity-id [entity-id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?entity-id
              :where
              [?entity-id :product/label ?e]]
            db entity-id)))
(def db (d/db conn))
(get-label-by-entity-id 101155069755477)


(def stock-data
  [{:stock/product (get-entity-id-by-label "lacoste")
    :order/stock   10}
   {:stock/product (get-entity-id-by-label "canada goose")
    :order/stock   8}
   {:stock/product (get-entity-id-by-label "mammut")
    :order/stock   6}
   {:stock/product (get-entity-id-by-label "husky")
    :order/stock   4}])
(d/transact conn {:tx-data stock-data})


(defn stock-check [entity-id]
  (ffirst (d/q
            '[:find ?size
              :in $ ?entity-id
              :where
              [?e :stock/product ?entity-id]
              [?e :order/stock ?size]]
            db entity-id)))
(def db (d/db conn))
(stock-check (get-entity-id-by-label "lacoste"))


;gerçek kullanıcı testi, return true if real user otherwise false
(defn user-check-by-id [user-id]
  (not (empty? (d/q
                 '[:find ?e
                   :in $ ?user-id
                   :where
                   [?e :user/id ?user-id]]
                 db user-id)))
  )
(user-check-by-id 1)

(defn get-user-entityid-by-userid [user-id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?user-id
              :where
              [?e :user/id ?user-id]]
            db user-id)))
(get-user-entityid-by-userid 1)

(def db (d/db conn))                                        ;;refresh database
(defn get-user-id-by-username [username]
  (ffirst (d/q
            '[:find ?user-id
              :in $ ?username
              :where
              [?e :user/name ?username]
              [?e :user/id ?user-id]]
            db username)))
(get-user-id-by-username "userdemo")

(defn get-username-by-user-id [user-id]
  (ffirst (d/q
            '[:find ?username
              :in $ ?user-id
              :where
              [?e :user/id ?user-id]
              [?e :user/name ?username]
              ]
            db user-id)))
(get-username-by-user-id 2)



;cart system
(def !my-cart
  (atom []))

;bu method ile eşyaları carta ekliyoruz, stock kontrolu yapıyoruz.
(defn stock-check-and-put-in-cart [user-id entity-id order-size]
  (if (>= (stock-check entity-id) order-size)
    (swap! !my-cart conj [(get-username-by-user-id user-id) (get-label-by-entity-id entity-id) order-size])
    (print "OUT OF STOCK
           Stock size is: " (stock-check entity-id))
    )
  )

(stock-check-and-put-in-cart 2 (get-entity-id-by-label "mammut") 2)

;bu method ile cartımızı bir table içerisinde kullanıcı adı marka ve sipariş miktarini gözlemleyebiliyoruz.
(for [[username label order-size] @!my-cart]
  (clerk/html [:table [:tr [:td username] [:td label] [:td order-size]]])
  )

;bu method ile satın alma işlemi tamamlanınca cart vectörünü tamamen temizliyoruz.
(defn remove-all-elements-in-vec-and-return-it
  [coll]
    (into (subvec coll 0 0))
    )


;cartı temizleme methodunu burada kullanıyoruz.
(swap! !my-cart remove-all-elements-in-vec-and-return-it)



;;working!
(defn stock-check-and-sell-item [user-id entity-id order-size]
  (if (>= (stock-check entity-id) order-size)
    (if (user-check-by-id user-id)
      ((d/transact conn {:tx-data [{:stock/product entity-id
                                    :order/stock   (- (stock-check entity-id) order-size)}]})
       (d/transact conn {:tx-data [{:order/product entity-id
                                    :order/user    (get-user-entityid-by-userid user-id)
                                    :order/size    order-size}]}))
      )
    (print "OUT OF STOCK!!
    Stock size is: " (stock-check entity-id)))
  (def db (d/db conn))                                      ;;refresh database
  )



;; ## ---------------------------------------------------------------------------------------------------------------
;1. ## Launch browser
;2. ## Navigate to url 'http://localhost:7779/'
;3. ## Click on 'Signup / Login' button
(clerk/html [:button {:type "button"} "Signup / Login"])
;4. ## Enter username password and email then  click 'Signup' button to create an account
(clerk/html [:div.text-field-container [:form#userForm {:class ""}
                                        [:div#userName-wrapper.mt-2.row [:div.col-md-3.col-sm-12 [:label#userName-label.form-label "Name/Id  "]] [:div.col-md-9.col-sm-12 [:input#userName.mr-sm-2.form-control {:autocomplete "off" :placeholder "Username goes here" :type "text"}]]]
                                        [:div#userEmail-wrapper.mt-2.row [:div.col-md-3.col-sm-12 [:label#userEmail-label.form-label "Email"]] [:div.col-md-9.col-sm-12 [:input#userEmail.mr-sm-2.form-control {:autocomplete "off" :placeholder "name@example.com" :type "email"}]]]
                                        [:div#userEmail-wrapper.mt-2.row [:div.col-md-3.col-sm-12 [:label#userEmail-label.form-label "Password"]] [:div.col-md-9.col-sm-12 [:input#userEmail.mr-sm-2.form-control {:autocomplete "off" :placeholder "Password" :type "Password"}]]]
                                        [:div.mt-2.justify-content-end.row [:div.text-right.col-md-2.col-sm-12 [:button#submit.btn.btn-primary {:type "button"} "Submit"]]] [:div#output.mt-4.row [:div.undefined.col-md-12.col-sm-12]]]])

(def user-data2
  [{:user/id       3
    :user/name     "bariscan"
    :user/password "123456"}
   ])
(d/transact conn {:tx-data user-data2})
(def db (d/db conn))                                        ;;refresh database


;5. ## user clicks products page to see all products
(clerk/html [:button {:type "button"} "Products"])

;6. ## user sees all of the products





(identity show-all-products)

;7. ## user puts a mammut boot in the cart and completes the purchase function

(stock-check (get-entity-id-by-label "mammut"))
;=> 6
;(stock-check-and-sell-item (get-entity-id-by-label "mammut") 2 (get-user-id-by-username "bariscan"))
(stock-check (get-entity-id-by-label "mammut"))
;=> 4

;8. ## user return products page

(identity show-all-products)
(stock-check (get-entity-id-by-label "lacoste"))
;=> 10
(stock-check (get-entity-id-by-label "mammut"))
;=> 4
(stock-check (get-entity-id-by-label "canada goose"))
;=> 8
(stock-check (get-entity-id-by-label "husky"))
;=> 4

;9. ## user buys 4 pieces of canada goose cabans
(stock-check (get-entity-id-by-label "canada goose"))
;=> 8
;(stock-check-and-sell-item (get-entity-id-by-label "canada goose") 2 (get-user-id-by-username "bariscan"))
(stock-check (get-entity-id-by-label "canada goose"))
;=> 6


;10. ## user return products page

(identity show-all-products)
(stock-check (get-entity-id-by-label "lacoste"))
;;=> 10
(stock-check (get-entity-id-by-label "mammut"))
;=> 4
(stock-check (get-entity-id-by-label "canada goose"))
;=> 6
(stock-check (get-entity-id-by-label "husky"))
;=> 4

;11. ## user buys 5 pieces of husky sleeping bags while there are no enough stocks
;(stock-check-and-sell-item (get-entity-id-by-label "husky") 5 (get-user-id-by-username "bariscan"))
;OUT OF STOCK!!
;    Stock size is:  4=> #'T01/db
(stock-check (get-entity-id-by-label "husky"))
;=> 4
