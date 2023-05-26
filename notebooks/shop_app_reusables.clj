(ns shop_app_reusables
  (:require [nextjournal.clerk :as clerk]
            [datomic.client.api :as d]))


;;queries
(defn get-entity-id-by-label [db label]
  (ffirst (d/q
            '[:find ?e
              :in $ ?label
              :where
              [?e :product/label ?label]]
            db label)))
(defn get-label-by-entity-id [db entity-id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?entity-id
              :where
              [?entity-id :product/label ?e]]
            db entity-id)))
(defn stock-check-by-label [db label]
  (ffirst (d/q
            '[:find ?size
              :in $ ?entity-id
              :where
              [?e :stock/product ?entity-id]
              [?e :stock/amount ?size]]
            db (get-entity-id-by-label db label))))
(defn user-validation-check-by-id [db user-id]
  (not (empty? (d/q
                 '[:find ?e
                   :in $ ?user-id
                   :where
                   [?e :user/id ?user-id]]
                 db user-id)))
  )
(defn get-user-entity-id-by-userid [db user-id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?user-id
              :where
              [?e :user/id ?user-id]]
            db user-id)))
(defn get-user-id-by-username [db username]
  (ffirst (d/q
            '[:find ?user-id
              :in $ ?username
              :where
              [?e :user/name ?username]
              [?e :user/id ?user-id]]
            db username)))
(defn get-username-by-user-id [db user-id]
  (ffirst (d/q
            '[:find ?username
              :in $ ?user-id
              :where
              [?e :user/id ?user-id]
              [?e :user/name ?username]
              ]
            db user-id)))

;;cart and active items list system
(def !my-cart
  (atom [])
  )
(def !my-items
  (atom [])
  )


(defn destruct-cart-items [my-cart]
  (for [[user product stock-amount] my-cart]
           [:tr [:td user] [:td product] [:td stock-amount]]
         )
  )

(defn destruct-active-items [my-items db]
  (for [[product stock-amount] my-items]
    [:tr [:td (get-label-by-entity-id db product)] [:td stock-amount]]
    )
  )


(defn existing-products [db]
  (for [[stock-amount product] (d/q
                                 '[:find ?name ?p
                                   :where
                                   [?e :stock/amount ?name]
                                   [?e :stock/product ?p]]
                                 db)]
    (swap! !my-items conj [stock-amount product])
    )
  )


(def cart-info                                              ;sideeffect
  (doall (clerk/html (into [:table [:tr
                                    [:th "User"]
                                    [:th "Product"]
                                    [:th "Stock Amount"]]] (destruct-cart-items @!my-cart))))
  )


;rfr: unquote splicing: https://clojuredocs.org/clojure.core/unquote-splicing
(def cart-info-2-yedek                                      ;sideeffect
  (clerk/html `[:table
                [:tr
                 [:th "User"]
                 [:th "Product"]
                 [:th "Stock Amount"]]
                ~@(destruct-cart-items @!my-cart)])
  )

(identity cart-info)


(defn show-all-products [db]                                ;sideeffect
  (existing-products db)
  (clerk/html `[:table
                [:tr
                 [:th "User"]
                 [:th "Product"]
                 [:th "Stock Amount"]]
                ~@(destruct-active-items @!my-items db)])
  )

(identity show-all-products)



;;functionalities
(defn add-new-user [db conn user-id username password-string]
  (d/transact conn {:tx-data [{:user/id       user-id
                               :user/name     username
                               :user/password password-string}
                              ]})
  (def db (d/db conn))
  )
(defn put-item-in-cart [db username label order-size]
  (if (>= (stock-check-by-label db label) order-size)
    (swap! !my-cart conj [username label order-size])
    (print "OUT OF STOCK
           Stock size is: " (stock-check-by-label db label))
    )
  )

(defn remove-all-elements-in-cart
  [coll]
  (into (subvec coll 0 0))
  )


(defn sell-all-items-in-cart [db conn]
  (def db (d/db conn))                                      ;;refresh database
  (doall (for [[username label order-size] @!my-cart]
           (if (>= (stock-check-by-label db label) order-size)
             (if (user-validation-check-by-id db (get-user-id-by-username db username))
               (do
                 (d/transact conn {:tx-data [{:stock/product (get-entity-id-by-label db label)
                                              :stock/amount  (- (stock-check-by-label db label) order-size)}]})
                 (d/transact conn {:tx-data [{:order/product (get-entity-id-by-label db label)
                                              :order/user    (get-user-entity-id-by-userid db (get-user-id-by-username db username))
                                              :order/size    order-size}]})
                 )
               (print "UNKNOWN USER!!")
               )
             (print "OUT OF STOCK!!
    Stock size is: " (stock-check-by-label db label))
             )
           )
         )
  (swap! !my-cart remove-all-elements-in-cart)
  (swap! !my-items remove-all-elements-in-cart)
  (def db (d/db conn))                                      ;;refresh database
  )


