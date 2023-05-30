(ns vector-of-text-debugging)

(defn regex-file-seq
  "Lazily filter a directory based on a regex."
  [re dir]
  (filter #(re-find re (.getPath %)) (file-seq dir)))

(mapv
  (fn [file] {:name (.getName file), :content (slurp file)})
  (filter
    (fn [file] (not (.isDirectory file)))
    (regex-file-seq #".*\.clj" (clojure.java.io/file "/Users/bariscanates/prj/electric/src/study/PostwalkDataStructures/Shining_data_structure_for_walk.clj"))))




(def f (fn [file] {:name (.getName file), :content (slurp file)}))

(def coll (filter
            (fn [file] (not (.isDirectory file)))
            (regex-file-seq #".*\.(clj[cs]?)$" (clojure.java.io/file "/Users/bariscanates/prj/electric/src/study/VeriAnalizProblemleri/D07c_volatile!_approach.clj"))))

;mapv function collection imzası ile çalışıyor.


(mapv f coll)

; çalışıyor
(identity coll)


(fn [file] {:name (.getName file), :content (slurp file)})
















