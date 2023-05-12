(ns user)


(comment
  (require '[nextjournal.clerk :as clerk])

  ;; start Clerk's built-in webserver on the default port 7777, opening the browser when done
  (clerk/serve! {:browse? true
                 :port 7779})

  (clerk/clear-cache!)

  ;; either call `clerk/show!` explicitly

  (clerk/show! "notebooks/S01_baris_examples.clj")
  (clerk/show! "notebooks/T01.clj")

  ;end
  ,)
