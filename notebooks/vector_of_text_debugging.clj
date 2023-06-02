(ns vector-of-text-debugging
  (:require [clojure.tools.analyzer.jvm :as ana]))


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



(->>  '(loop [result [] x 5]
        (if (zero? x)
          result
          (recur (map result x) (dec x))))

     (ana/analyze)
     (ana-tree-seq)
     (filter (ana-var=-fn 'clojure.core/map))
     (count))
; ⇒ 2





;--------
