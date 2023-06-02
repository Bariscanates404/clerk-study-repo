(ns T02)
(require '[datomic.client.api :as d]
         '[clojure.data.csv :as csv]
         '[clojure.java.io :as io]
         )
(def client (d/client {:server-type :dev-local
                       :storage-dir :mem
                       :system      "ci"}))
(d/create-database client {:db-name "db01"})
(def conn (d/connect client {:db-name "db01"}))
(def db (d/db conn))                                        ;;refresh database
(def db-schema
  [{:db/ident       :function/name
    :db/valueType   :db.type/string
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident       :function/sum
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])
(d/transact conn {:tx-data db-schema})
(def db (d/db conn))


(def type-vec ["accessor" "aclone" "add-classpath" "add-tap" "add-watch" "agent" "agent-error" "agent-errors" "aget" "alength" "alias" "all-ns" "alter" "alter-meta!" "alter-var-root" "amap" "ancestors" "and " "any?" "apply" "areduce" "array-map" "as->" "aset" "aset-boolean" "aset-byte" "aset-char" "aset-double" "aset-float" "aset-int" "aset-short" "assert" "assoc" "assoc!" "assoc-in" "associative?" "atom" "await" "await-for" "await1"
               "bases" "bean" "bigdec" "bigint" "biginteger" "binding" "bit-and" "bit-and-not" "bit-clear" "bit-flip" "bit-not" "bit-or" "bit-set" "bit-shift-left" "bit-shift-right" "bit-test" "bit-xor" "boolean" "boolean-array" "bytes" "bytes?"
               "case" "cast" "cat" "catch" "char" "char-array" "char-escape-string" "char-name-string" "char?" "chars" "chunk" "chunk-append" "chunk-buffer" "chunk-cons" "chunk-first" "chunk-rest" "chunk-next" "chunked-seq?" "class" "class?" "clear-agent-errors" "clojure-version" "coll?" "comment" "commute" "comp" "comparator" "compare" "compare-and-set!" "compile" "complement" "completing" "concat" "cond" "cond->" "cond->>" "condp" "conj" "conj!" "cons" "constantly" "construct-proxy" "contains?" "count" "counted?" "create-ns" "create-struct" "cycle"
               "dec" "dec'" "decimal?" "declare" "dedupe" "default-data-readers" "delay" "delay?" "deliver" "denominator" "derive" "descendants" "destructure" "disj" "disj!" "dissoc" "dissoc!" "distinct" "distinct?" "do" "doall" "dorun" "doseq" "dosync" "dotimes" "doto" "double" "double-array" "double?" "drop" "drop-last" "drop-while"
               "eduction" "empty?" "empty" "ensure" "ensure-reduced" "enumeration-seq" "error-handler" "error-mode" "eval" "even?" "every-pred" "every?" "ex-cause" "ex-data" "ex-info" "ex-message" "extend" "extend-protocol" "extend-type" "extenders" "extends?"
               "false?" "ffirst" "file-seq" "filter " "filterv" "finally" "find" "find-keyword" "find-ns" "find-protocol-impl" "find-protocol-method" "find-var" "first " "flatten" "float" "float-array" "float?" "floats" "flush" "fn?" "fnext" "fnil" "for" "force" "format" "frequencies" "future" "future-call" "future-cancel" "future-cancelled?" "future-done?" "future?"
               "gen-class" "gen-interface" "gensym" "get" "get-in" "get-method" "get-proxy-class" "get-thread-bindings" "get-validator" "group-by"
               "halt-when" "hash" "hash-combine" "hash-map" "hash-ordered-coll" "hash-set" "hash-unordered-coll"
               "ident?" "identity" "identical?" "if" "if-let" "if-not" "if-some" "ifn?" "import" "in-ns" "inc" "inc'" "indexed?" "init-proxy" "inst-ms*" "inst-ms" "inst?" "instance?" "int" "int-array" "int?" "integer?" "interleave" "intern" "interpose" "into" "into-array" "ints" "io!" "isa?" "iterate" "iterator-seq"
               "juxt"
               "keep" "keep-indexed" "key" "keys" "keyword" "keyword?"
               "last" "lazy-cat" "lazy-seq" "line-seq" "list" "list*" "list?" "load" "load-file" "load-reader" "load-string" "loaded-libs" "locking" "long" "long-array" "longs" "loop"
               "macroexpand" "macroexpand-1" "make-array" "make-hierarchy" "map " "map-entry?" "map-indexed" "map?" "mapcat" "mapv" "max" "max-key" "memfn" "memoize" "merge" "merge-with" "meta" "method-sig" "methods" "min" "min-key" "mix-collection-hash" "mod" "monitor-enter" "monitor-exit" "munge"
               "name " "namespace" "namespace-munge" "nat-int?" "neg-int?" "neg?" "new" "newline" "next" "nfirst" "nil?" "nnext" "not" "not-any?" "not-empty" "not-every?" "not=" "ns-aliases" "ns-imports" "ns-interns" "ns-map" "ns-name" "ns-publics" "ns-refers" "ns-resolve" "ns-unalias" "ns-unmap" "nth" "nthnext" "nthrest" "num" "number?" "numerator"
               "object-array" "odd?" "or "
               "parents" "partial" "partition" "partition-all" "partition-by" "pcalls" "peek" "persistent!" "pmap" "pop" "pop!" "pop-thread-bindings" "pos-int?" "pos?" "pr" "pr-str" "prefer-method" "prefers" "primitives-classnames" "promise" "proxy-call-with-super" "proxy" "proxy-mappings" "proxy-name" "proxy-super" "push-thread-bindings" "pvalues"
               "qualified-ident?" "qualified-keyword?" "qualified-symbol?" "quot" "quote"
               "rand" "rand-int" "rand-nth" "random-sample" "range" "ratio?" "rational?" "rationalize" "re-find" "re-groups" "re-matcher" "re-matches" "re-pattern" "re-seq" "read" "read+string" "read-line" "read-string" "reader-conditional" "reader-conditional?" "realized?" "record?" "recur" "reduce " "reduce-kv" "reduced" "reduced?" "reductions" "ref" "ref-history-count" "ref-max-history" "ref-min-history" "ref-set" "refer" "refer-clojure" "reify" "release-pending-sends" "rem" "remove" "remove-all-methods" "remove-method" "remove-ns" "remove-tap" "remove-watch" "repeat" "repeatedly" "replace" "replicate" "require" "requiring-resolve" "reset!"
               "reset-meta!" "reset-vals!" "resolve" "rest" "restart-agent" "resultset-seq" "reverse" "reversible?" "rseq" "rsubseq" "run!"
               "satisfies?" "second" "select-keys" "send" "send-off" "send-via" "seq " "seq?" "seqable?" "seque" "sequence" "sequential?" "set" "set!" "set-agent-send-executor!" "set-agent-send-off-executor!" "set-error-handler!" "set-error-mode!" "set-validator!" "set?" "short-array" "shorts" "shuffle" "shutdown-agents" "simple-ident?" "simple-keyword?" "simple-symbol?" "slurp" "some" "some->" "some->>" "some-fn" "some?" "sort" "sort-by" "sorted-map-by" "sorted-set-by" "sorted-set" "sorted-map" "sorted?" "special-symbol?" "spit" "split-at" "split-with" "StackTraceElement" "str " "string?" "struct" "struct-map" "subs" "subseq" "subvec" "supers"
               "swap!" "swap-vals!" "symbol" "symbol?" "sync"
               "tagged-literal" "tagged-literal?" "take" "take-last" "take-while" "take-nth" "tap>" "the-ns" "thread-bound?" "throw" "Throwable->map" "time " "to-array-2d" "to-array" "trampoline" "transduce" "transient" "tree-seq" "true?" "try" "type"
               "unchecked-add" "unchecked-add-int" "unchecked-byte" "unchecked-char" "unchecked-dec" "unchecked-dec-int" "unchecked-divide-int" "unchecked-double" "unchecked-float" "unchecked-inc-int" "unchecked-inc" "unchecked-int" "unchecked-long" "unchecked-multiply" "unchecked-multiply-int" "unchecked-negate-int" "unchecked-negate" "unchecked-short" "unchecked-subtract-int" "unchecked-subtract" "underive" "unreduced" "unsigned-bit-shift-right" "update" "update-in" "update-proxy" "uri?" "use" "uuid?"
               "val" "vals" "var" "var-get" "var-set" "var?" "vary-meta" "vec" "vector" "vector-of" "vector?" "volatile!" "volatile?" "vreset!" "vswap!"
               "when" "when-first" "when-let" "when-not" "when-some" "while" "with-bindings*" "with-bindings" "with-in-str" "with-local-vars" "with-meta" "with-open" "with-out-str" "with-local-vars" "with-meta" "with-open" "with-precision" "with-redefs-fn" "with-redefs"
               "xml-seq" "zero?" "zipmap"])

(def db (d/db conn))
(defn regex-file-seq
  "filters directories based regex exp"
  [regex dir]
  (filter #(re-find regex (.getPath %)) (file-seq dir)))


(defn add-a-type-into-schema "adds just one item in functions schema" [type-str]
  (d/transact conn {:tx-data [{:function/name type-str
                                  :function/sum  0
                                  }
                                 ]})
  (def db (d/db conn))
  )

(defn create-type-schema "adds multiple items in functions schema"
  [type-vec]
  (doall (for [len (range 0 (count type-vec))]
           (add-a-type-into-schema (get type-vec len))
           )
         )
  )

(create-type-schema type-vec)

(def !type "helps to run increase-usages function inside of main functions for loop"
  (atom ["String"])
  )
(defn f "searchs the given value(inside !type atom) inside given text(vector-0f-texts)"
  [text]
  (count (re-seq (re-pattern (str "\\Q" (get @!type 0) "\\E" "[^a-zA-Z0-9*+!\\-_'?]")) text)))


(def vector-0f-texts "gets all the project files and return them as map of strings"
  (mapv
    (fn [file] {:name (.getName file), :content (slurp file)})
    (filter
      (fn [file] (not (.isDirectory file)))
      (regex-file-seq #".*\.(clj[cs]?)$" (clojure.java.io/file "/Users/bariscanates/repostoanalysis/"))))
  )


(defn function-usages "shows  the number of given functions usages which are saved db until now on"
  [func-name]
  (ffirst (d/q
            '[:find ?name
              :in $ ?func-name
              :where
              [?e :function/name ?func-name]
              [?e :function/sum ?name]]
            db func-name))
  )

(defn increase-usages "counts the usage sum of given function name and writes into the db the sum of old and new usage numbers"
  [func-name-string func-usage-to-sum]
  (def db (d/db conn))
  (d/transact conn {:tx-data [{:function/name func-name-string
                                  :function/sum  (+ (function-usages func-name-string) func-usage-to-sum)}
                                 ]})
  (def db (d/db conn))
  )



(defn main-function "checks all given function(by function type vector) usages into given project and writes their sum into db"
  [type-coll]
  (doall (for [len (range 0 (count type-coll))]
           (do
             (reset! !type [(get type-vec len)])
             (increase-usages (get type-coll len) (reduce + (into [] (map f (map :content vector-0f-texts)))))
             )
           )
         )
  )
(main-function type-vec)

(def all-func-usages "returns all saved function usages into db"
  (reverse (->> (d/q
                  '[:find ?func-name ?sum
                    :where
                    [?e :function/name ?func-name]
                    [?e :function/sum ?sum]]
                  db)
                (sort-by last)
                )
           )
  )
(def result-page
  (into [] all-func-usages))

(defn create-csv "creates and writes given data into a cvs file"
  [res-vec]
  (for [len (range 0 (count res-vec))]
    (with-open [writer (io/writer "tmp/foo.csv" :append true)]
      (csv/write-csv writer [[(str (str (+ len 1) "-") (str "      " (str (get (get res-vec len) 0))) (str "      " (get (get res-vec len) 1)))]])
      )
    )
  )

(create-csv result-page)
(println (slurp "tmp/foo.csv"))
