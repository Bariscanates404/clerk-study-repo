(ns shop_app_reusables
  (:require [T01 :as app]))




(defn get-entity-id-by-label [label]
  (ffirst (d/q
            '[:find ?e
              :in $ ?label
              :where
              [?e :product/label ?label]]
            db label)))
(app/db)

(defn get-label-by-entity-id [entity-id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?entity-id
              :where
              [?entity-id :product/label ?e]]
            db entity-id)))
(def db (d/db conn))




(defn stock-check-by-label [label]
  (ffirst (d/q
            '[:find ?size
              :in $ ?entity-id
              :where
              [?e :stock/product ?entity-id]
              [?e :stock/amount ?size]]
            db (get-entity-id-by-label label))))

(def db (d/db conn))


;gerçek kullanıcı testi, return true if real user otherwise false
(defn user-validation-check-by-id [user-id]
  (not (empty? (d/q
                 '[:find ?e
                   :in $ ?user-id
                   :where
                   [?e :user/id ?user-id]]
                 db user-id)))
  )

(defn get-user-entity-id-by-userid [user-id]
  (ffirst (d/q
            '[:find ?e
              :in $ ?user-id
              :where
              [?e :user/id ?user-id]]
            db user-id)))


(def db (d/db conn))                                        ;;refresh database
(defn get-user-id-by-username [username]
  (ffirst (d/q
            '[:find ?user-id
              :in $ ?username
              :where
              [?e :user/name ?username]
              [?e :user/id ?user-id]]
            db username)))


(defn get-username-by-user-id [user-id]
  (ffirst (d/q
            '[:find ?username
              :in $ ?user-id
              :where
              [?e :user/id ?user-id]
              [?e :user/name ?username]
              ]
            db user-id)))

(def show-all-products
  (clerk/html [:table
               [:tr [:th "Product"] [:th "Stock Amount"]]
               (for [[stock-amount product] (d/q
                                              '[:find ?name ?p
                                                :where
                                                [?e :stock/amount ?name]
                                                [?e :stock/product ?p]]
                                              db)]
                 [:tr [:td (get-label-by-entity-id product)] [:td stock-amount]]

                 )
               ]
              )
  )




;cart system
(def !my-cart
  (atom [])
  )

(defn cart-info [cart]
  (clerk/html [:table
               [:tr [:th "User"] [:th "Product"] [:th "Stock Amount"]]
               (for [[user product stock-amount] cart]
                 [:tr [:td user] [:td product] [:td stock-amount]]
                 )
               ]
              )
  )

;bu method ile eşyaları carta ekliyoruz, stock kontrolu yapıyoruz.
(defn put-item-in-cart [username label order-size]
  (if (>= (stock-check-by-label label) order-size)
    (swap! !my-cart conj [username label order-size])
    (print "OUT OF STOCK
           Stock size is: " (stock-check-by-label label))
    )
  )


;bu method ile satın alma işlemi tamamlanınca cart vectörünü tamamen temizliyoruz.
(defn remove-all-elements-in-cart
  [coll]
  (into (subvec coll 0 0))
  )

;;working!
(defn sell-all-items-in-cart []
  (for [[username label order-size] (for [len (range 0 (count @!my-cart))]
                                      (get @!my-cart len)
                                      )]
    (if (>= (stock-check-by-label label) order-size)
      (if (user-validation-check-by-id (get-user-id-by-username username))
        ((d/transact conn {:tx-data [{:stock/product (get-entity-id-by-label label)
                                      :stock/amount  (- (stock-check-by-label label) order-size)}]})
         (d/transact conn {:tx-data [{:order/product (get-entity-id-by-label label)
                                      :order/user    (get-user-entity-id-by-userid (get-user-id-by-username username))
                                      :order/size    order-size}]}))
        (print "OUT OF STOCK!!
    Stock size is: " (stock-check-by-label label)))
      )
    )
  (def db (d/db conn))                                      ;;refresh database
  (swap! !my-cart remove-all-elements-in-cart)
  )
(sell-all-items-in-cart)


