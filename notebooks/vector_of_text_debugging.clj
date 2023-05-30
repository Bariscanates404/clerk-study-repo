(ns vector-of-text-debugging
  (:require [clojure.tools.analyzer.jvm :as ana]))

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



(identity coll)

(prn
  (frequencies
    (filter
      #{ 'reduce-kv 'str-paths 'vals 'acc}
      (tree-seq (some-fn list? vector? map?) seq (read-string "reduce-kv")))))








(macroexpand-1 '(->> (read-string "") ; `s` is from your example
                    (tree-seq (some-fn list? vector? map?) seq) ; "flatten" the tree
                    (filter #{'vals 'acc 'reduce-kv 'str-paths}) ; only pick symbols with the names map or acc
                    (frequencies)
                    (prn))

               )




(prn
  (frequencies
    (filter
      #{ 'reduce-kv 'str-paths 'vals 'acc}
      (tree-seq (some-fn list? vector? map?) seq (read-string "reduce-kv")))))













(defn ana-branch?
  "tree-seq branch? predicate for clojure.tools.analyzer.jmv/analyze"
  [node]
  (or (and (map? node) (:children node))
      (vector? node)))

(defn ana-children
  "tree-seq children extractor for clojure.tools.analyzer.jmv/analyze"
  [node]
  (if (map? node)
    (map node (:children node))
    (seq node)))

(defn ana-tree-seq
  "Create a tree-seq over the result from clojure.tools.analyzer.jmv/analyze"
  [root]
  (tree-seq ana-branch? ana-children root))

(defn ana-var=-fn
  "Creates a predicate function to check, if the given node is a :var :op and the symbol of :var is the given sym"
  [sym]
  (fn [node]
    (and (map? node)
         (= :var (:op node))
         (= sym (symbol (:var node))))))

; ---

(->> '(->>
        [{:a 1}]
        (map (fn [map]
               (update map :a vector)))
        (mapcat (partial (comp (partial map inc) :a))))

     (ana/analyze)
     (ana-tree-seq)
     (filter (ana-var=-fn 'clojure.core/map))
     (count))
; ⇒ 2

