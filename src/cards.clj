(ns cards)

;; Think about how you would model a deck of cards in Java using enums, classes, etc

(def suit? #{:club :diamond :heart :spade})
(def rank? (into #{:jack :queen :king :ace} (range 2 11)))
(def full-deck (for [suit suit? rank rank?] [rank suit]))

(comment
  (take 3 full-deck)
  )

(defn deal
  "Deal a full deck into n even hands"
  [n]
  (->> full-deck
    shuffle
    (partition (quot 52 n))
    (take n)))

(comment
  (deal 4)
  (map count (deal 5))

  (doseq [i (range 1 14)]
    (let [dealt (deal i)]
      (println i (count dealt) (map count dealt))))

  )