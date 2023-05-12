(ns user)


(comment
  (require '[nextjournal.clerk :as clerk])

  ;; start Clerk's built-in webserver on the default port 7777, opening the browser when done
  (clerk/serve! {:browse? true
                 :port 7779})

  (clerk/clear-cache!)

  ;; either call `clerk/show!` explicitly
  (clerk/show! "notebooks/e01_form_submit.clj")
  (clerk/show! "notebooks/e02_entity_types.clj")
  (clerk/show! "notebooks/e03-clerk-functionalities.md")
  (clerk/show! "notebooks/e01_form_submit.clj")
  (clerk/show! "notebooks/e01_form_submit.clj")
  (clerk/show! "notebooks/e01_form_submit.clj")
  (clerk/show! "notebooks/e07_baris_denemeler.clj")
  (clerk/show! "notebooks/T01.clj")

  ;end
  ,)
