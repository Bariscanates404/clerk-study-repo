(ns repo-analysis-app-resuables)
(require
  '[clojure.data.csv :as csv]
  '[clojure.java.io :as io]
  )
(defn regex-file-seq
  "filters directories based regex exp"
  [regex dir]
  (filter #(re-find regex (.getPath %)) (file-seq dir)))


(defn create-cvs "creates and writes given data into a cvs file"
  [res-vec]
  (for [len (range 0 (count res-vec))]
    (with-open [writer (io/writer "tmp/foo.csv" :append true)]
      (csv/write-csv writer [[(str (str (+ len 1) "-") (str "      " (str (get (get res-vec len) 0))) (str "      " (get (get res-vec len) 1)))]])
      )
    )
  )