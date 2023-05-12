(ns e07_baris_denemeler)

(require '[nextjournal.clerk :as clerk])


;; ## Clojure Data

;hiccup html element çalışması // w3schoolsdan destek alalım.
(clerk/html [:div "As Clojurians we " [:em "really"] " enjoy hiccup"])

;html headings
(clerk/html [:div [:h2 "Baslik!"] [:h3 "Baslik!"] [:h4 "Baslik!"] [:h5 "Baslik!"]])


;html headings
(clerk/html [:div [:p [:h2 "paragraf!"]] [:p "paragraf!"] [:p "paragraf!"]])

;highlighting a text
(clerk/html [:p "This tag will " [:mark "highlight"] " the text."])

;using anchor
(clerk/html [:a {:href "second.html"} "Click for Second Page"])

;img
(clerk/html [:img {:src "good_morning.jpg" :alt "Good Morning Friends"}])

;building table
(clerk/html [:table
             [:tr [:th "First_Name"] [:th "Last_Name"] [:th "Marks"]]
             [:tr [:td "Sonoo"] [:td "Jaiswal"] [:td "60"]]
             [:tr [:td "James"] [:td "William"] [:td "80"]]
             [:tr [:td "Swati"] [:td "Sironi"] [:td "82"]]
             [:tr [:td "Chetna"] [:td "Singh"] [:td "72"]]])

;html list
(clerk/html [:ol
             [:li "Aries"]
             [:li "Bingo"]
             [:li "Leo"]
             [:li "Oracle"]])

;form elements
(clerk/html [:form "First Name:" [:input {:type "text" :name "firstname"}] [:br] "Last Name:" [:input {:type "text" :name "lastname"}] [:br]])

;dropdown element
(clerk/html [:select "select here" [:option "us"] [:option "tr"] [:option "de"]])

;radio button element
(clerk/html [:div [:input#html {:type "radio" :name "fav_language" :value "HTML"}] [:label {:for "html"} "HTML"] [:br] [:input#css {:type "radio" :name "fav_language" :value "CSS"}] [:label {:for "css"} "CSS"] [:br] [:input#javascript {:type "radio" :name "fav_language" :value "JavaScript"}] [:label {:for "javascript"} "JavaScript"]])

;checkbox
(clerk/html [:html
             [:head]
             [:body
              [:form "Programming Languages:" [:br]
               [:input#C {:type "checkbox" :name "C" :value "C"}]
               [:label "C"] [:br]
               [:input#Java {:type "checkbox" :name "Java" :value "Java" :checked "?yes?/"}]
               [:label "Java"] [:br]
               [:input#Python {:type "checkbox" :name "Python" :value "Python"}]
               [:label "Python"] [:br]
               [:input#PHP {:type "checkbox" :name "PHP" :value "PHP"}]
               [:label "PHP"]]]])

; ## improved examples
;text boxes
(clerk/html [:div.text-field-container [:form#userForm {:class ""} [:div#userName-wrapper.mt-2.row [:div.col-md-3.col-sm-12 [:label#userName-label.form-label "Full Name"]] [:div.col-md-9.col-sm-12 [:input#userName.mr-sm-2.form-control {:autocomplete "off" :placeholder "Full Name" :type "text"}]]] [:div#userEmail-wrapper.mt-2.row [:div.col-md-3.col-sm-12 [:label#userEmail-label.form-label "Email"]] [:div.col-md-9.col-sm-12 [:input#userEmail.mr-sm-2.form-control {:autocomplete "off" :placeholder "name@example.com" :type "email"}]]] [:div#currentAddress-wrapper.mt-2.row [:div.col-md-3.col-sm-12 [:label#currentAddress-label.form-label "Current Address"]] [:div.col-md-9.col-sm-12 [:textarea#currentAddress.form-control {:placeholder "Current Address" :rows "5" :cols "20"}]]] [:div#permanentAddress-wrapper.mt-2.row [:div.col-md-3.col-sm-12 [:label#permanentAddress-label.form-label "Permanent Address"]] [:div.col-md-9.col-sm-12 [:textarea#permanentAddress.form-control {:rows "5" :cols "20"}]]] [:div.mt-2.justify-content-end.row [:div.text-right.col-md-2.col-sm-12 [:button#submit.btn.btn-primary {:type "button"} "Submit"]]] [:div#output.mt-4.row [:div.undefined.col-md-12.col-sm-12]]]])

;check boxes
(clerk/html [:form.form
             [:div.form-group
              [:label "Default Checkboxes"]
              [:div.checkbox-list
               [:label.checkbox
                [:input {:type "checkbox" :name "Checkboxes1"}]
                [:span] "Default"]
               [:label.checkbox.checkbox-disabled
                [:input {:type "checkbox" :disabled "disabled" :checked "checked" :name "Checkboxes1"}]
                [:span] "Disabled"]
               [:label.checkbox
                [:input {:type "checkbox" :checked "checked" :name "Checkboxes1"}]
                [:span] "Checked"]]]
             [:div.form-group
              [:label "Inline Checkboxes"]
              [:div.checkbox-inline
               [:label.checkbox
                [:input {:type "checkbox" :name "Checkboxes2"}]
                [:span] "Option 1"]
               [:label.checkbox
                [:input {:type "checkbox" :name "Checkboxes2"}]
                [:span] "Option 2"]
               [:label.checkbox
                [:input {:type "checkbox" :name "Checkboxes2"}]
                [:span] "Option 3"]]
              [:span.form-text.text-muted "Some help text goes here"]]
             [:div.form-group
              [:label "Inline Checkboxes"]
              [:div.checkbox-inline
               [:label.checkbox
                [:input {:type "checkbox" :checked "checked" :name "Checkboxes3"}]
                [:span] "Option 1"]
               [:label.checkbox
                [:input {:type "checkbox" :name "Checkboxes3"}]
                [:span] "Option 2"]
               [:label.checkbox
                [:input {:type "checkbox" :checked "checked" :name "Checkboxes3"}]
                [:span] "Option 3"]]
              [:span.form-text.text-muted "Some help text goes here"]]
             [:div.form-group
              [:label "Large Size"]
              [:div.checkbox-inline
               [:label.checkbox.checkbox-lg
                [:input {:type "checkbox" :checked "checked" :name "Checkboxes3_1"}]
                [:span] "Option 1"]
               [:label.checkbox.checkbox-lg
                [:input {:type "checkbox" :name "Checkboxes3_1"}]
                [:span] "Option 2"]
               [:label.checkbox.checkbox-lg
                [:input {:type "checkbox" :name "Checkboxes3_1"}]
                [:span] "Option 3"]]
              [:span.form-text.text-muted "Some help text goes here"]]
             [:div.form-group
              [:label "Square Style"]
              [:div.checkbox-inline
               [:label.checkbox.checkbox-square
                [:input {:type "checkbox" :checked "checked" :name "Checkboxes13_1"}]
                [:span] "Option 1"]
               [:label.checkbox.checkbox-square
                [:input {:type "checkbox" :name "Checkboxes13_1"}]
                [:span] "Option 2"]
               [:label.checkbox.checkbox-square
                [:input {:type "checkbox" :name "Checkboxes13_1"}]
                [:span] "Option 3"]]
              [:span.form-text.text-muted "Some help text goes here"]]
             [:div.form-group
              [:label "Rounded Style"]
              [:div.checkbox-inline
               [:label.checkbox.checkbox-rounded
                [:input {:type "checkbox" :checked "checked" :name "Checkboxes15_1"}]
                [:span] "Option 1"]
               [:label.checkbox.checkbox-rounded
                [:input {:type "checkbox" :name "Checkboxes15_1"}]
                [:span] "Option 2"]
               [:label.checkbox.checkbox-rounded
                [:input {:type "checkbox" :name "Checkboxes15_1"}]
                [:span] "Option 3"]]
              [:span.form-text.text-muted "Some help text goes here"]]])

;web tables
(clerk/html [:div [:div.mb-3 "Do you like the clerk?"] [:div.custom-control.custom-radio.custom-control-inline [:input#yesRadio.custom-control-input {:type "radio" :name "like"}] [:label.custom-control-label {:for "yesRadio"} "Yes"]] [:div.custom-control.custom-radio.custom-control-inline [:input#impressiveRadio.custom-control-input {:type "radio" :name "like"}] [:label.custom-control-label {:for "impressiveRadio"} "Impressive"]] [:div.custom-control.disabled.custom-radio.custom-control-inline [:input#noRadio.custom-control-input.disabled {:type "radio" :disabled "" :name "like"}] [:label.custom-control-label.disabled {:for "noRadio"} "No"]]])



(require '[datomic.client.api :as d])

;; Memory storage
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))

(d/create-database client {:db-name "db02"})

(def conn (d/connect client {:db-name "db02"}))


(def schema-1
  [{:db/ident       :label
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :type
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :model-no
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}])

(d/transact conn {:tx-data schema-1})

@(def data
   [{:label "lacoste"
     :type       "urban clothing"
     :model-no      "polo shirt"}
    {:label "canada goose"
     :type       "jackets"
     :model-no      "caban"}
    {:label "mammut"
     :type       "boots"
     :model-no      "hiking boots"}
    {:label "husky"
     :type       "sleeping bags"
     :model-no      "arnapurna"}])
;=> [{:ogrenci_id 101, :isim "Ali Niyazi", :sehir "Bursa"} {:ogrenci_id 102, :isim "Veli Şimşek", :sehir "Kütahya"}]

(d/transact conn {:tx-data data})
(def db (d/db conn))

(d/q
  '[:find ?e
    :where
    [?e :label "lacoste"]]
  db)
;=> [[79164837199948]]

(d/q
  '[:find ?i
    :where
    [?e :sehir "Bursa"]
    [?e :isim ?i]]
  db)
;=> [["Ali Niyazi"]]

(d/q
  '[:find ?oid
    :where
    [?e :sehir "Bursa"]
    [?e :ogrenci_id ?oid]]
  db)
;=> [[101]]

(d/q
  '[:find ?oid
    :where
    [?e :sehir "Bursa"]
    [?e :ogrenci_id ?oid]]
  db)

; sehir değeri Bursa olan entitylerin tüm atributlarını görmek istiyorum
(d/q
  '[:find ?a
    :where
    [?e :sehir "Bursa"]
    [?e ?a _]]
  db)
;=> [[73] [74] [75]]


(d/q
  '[:find ?aname
    :where
    [?e :sehir "Bursa"]
    [?e ?a _]
    [?a :db/ident ?aname]]
  db)
;=> [[:isim] [:ogrenci_id] [:sehir]]
