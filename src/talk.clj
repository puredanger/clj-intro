;; Why Clojure?
;; - Alex Miller, Cognitect

;; ================================================================================================
;; Problems I've encountered in Java    ->    Answers found in Clojure
;; -----------------------------------        -------------------------------------------------
;; Mutable objects/equality/locking     ->    Immutable values and collections
;; Bespoke data interfaces (classes)    ->    Data literals, generic data interfaces
;; Conflation of identity + state       ->    State transition model, atoms, refs
;; Abstraction limits/design patterns   ->    Functions, macros (syntax)
;; Inflexible polymorphism              ->    Open extensibility (multimethods, protocols)
;; Verbosity, compile cycle             ->    REPL development
;; Brittleness from changing reqs       ->    Growth not breakage, open maps, open extensions
;; ================================================================================================

;; REPL

;; Simple values
5
0.5
1/3
"hello"
\a
:keyword
true
nil

;; Collections - immutable!
[1 2 3]
[1, 2, 3] ;; commas are whitespace
'(1 2 3)
#{1 2 3}
{:a 1 :b 2}

;; Questions?

;; Function invocation
(+ 1 1)
(count #{1 2 3})
(conj [1 2 3] 4)
(contains? {:a 1 :b 2} :a)

;; Vars - global definitions
(def x 5)
x
(+ x 1)

;; Function definition
(fn [x] (+ x 2))

(defn capitalize
  "Capitalize first character of string"
  [s]
  (str
    (Character/toUpperCase (first s))
    (subs s 1)))

(capitalize "abc")

;; but what about?
(capitalize "")
(capitalize nil)

;; Evaluation - expressions, symbols, lists, quoting
(1 2 3)
'(1 2 3)
(quote (1 2 3))

;; Special forms - def fn quote if do let loop/recur try throw .
(if (< (rand) 0.5) :low :high)
(let [a 1
      b 2
      c (+ b 1)]
  (+ a b c))
a

;; Sequences - logical list abstraction
(range 10)
(map inc [1 2 3])
(filter odd? (range 10))
(reduce + (map inc (filter odd? (range 10))))

;; Thread-last
(->> (range 10)
  (filter odd?)
  (map inc)
  (reduce +))

;; Infinite sequences
(take 10 (cycle [1 2]))
(take 10 (repeat 100))
(map +
  (cycle [1 2])
  (repeat 100)
  [9 8 7 6 5 4])
(zipmap [:alex :bob :cathy] (repeat 0))

;; Maps for information

(def person
  {:first     "Alex"
   :last      "Miller"
   :twitterID "puredanger"})

(get person :first)

(person :first)
(:first person)
(:first {})

(def people [{:first "Alex" :last "Miller" :twitterID "puredanger"}
             {:first "Rich" :last "Hickey" :twitterID "richhickey"}
             {:first "Stuart" :last "Halloway" :twitterID "stuarthalloway"}])
(map :twitterID people)

;; Questions?

;; Destructuring

(def x [1 2 3])

(defn p [v]
  (let [a (nth v 0)
        b (nth v 1)
        c (nth v 2)]
    (println a b c)))

(defn p2 [[a b c]]
  (println a b c))

(defn full-name
  [{:keys [first last]}]
  (str first " " last))

(full-name person)
(map full-name people)

;; Namespaces
(require '[clojure.string :as str])
(str/join ":" (range 10))

;; Discoverability
(dir clojure.string)
(dir str)
(source str/join)
(apropos "join")
(javadoc "java.lang.String")

;; Java interop
(import '[java.io File])
(def talk-file (File. "src/talk.clj"))
(.exists talk-file)
(.toPath talk-file)

(def talk (slurp talk-file))
(->> talk (map #(Character/toLowerCase %)) (filter #{\a \e \i \o \u}) frequencies)

(class "abc")
(run! #(println (.toString %)) (.getDeclaredMethods String))
(bean (java.util.Date.))

(def t (Thread. (fn [] (println "hi" (.getName (Thread/currentThread))))))
(.start t)

(import [java.util ArrayList])
(def java-list
  (doto (ArrayList.)
    (.add 1)
    (.add 2)
    (.add 3)))
java-list
(= java-list [1 2 3])
(ancestors (class [1 2 3]))

(try
  (/ 1 0)
  (catch Exception e
    (println (.getMessage e))))

;; Integrating Clojure into a Java code base -->

;; Concurrency and state

(future (dotimes [i 10] (println ".") (Thread/sleep 2000)))
(+ 1 1)

(def scores (atom []))
@scores
(swap! scores conj 100)
@scores

(def savings (ref 100))
(def checking (ref 0))
(defn transfer
  [from to amt]
  (dosync
    (alter from - amt)
    (alter to + amt))
  nil)

(transfer savings checking 20)
[@savings @checking]

(def savings (ref 1000000))
(def checking (ref 0))

(dotimes [i 1000] (future (transfer savings checking 1000)))
[@savings @checking]

;; Reader - syntax vs semantics
(+ 1 2)

;; REPL revisited
(println (eval (read-string "(+ 1 2)")))

;; Macros

(unless (System/getProperty "PROD")
  (println "Dev mode")
  (println))

(defmacro unless
  [condition & body]
  `(when (not ~condition)
     ~@body))

(System/setProperty "PROD" "true")

(source when-not)

;; What about?
;; - Web stuff?
;; - Front end?
;; - Databases?
;; - UIs?
;; - Machine learning / data science?
;; - Shell scripts?

;; Questions?