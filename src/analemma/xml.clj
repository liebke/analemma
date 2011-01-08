(ns analemma.xml)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; XML FUNCTIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-name [tag]
  (name (first tag)))

(defn get-attrs [[name & content]]
  (if (map? (first content)) (first content) {}))

(defn get-content [[name & content]]
  (if (map? (first content)) (rest content) content))

(defn emit-attrs [attrs]
  (when attrs
    (reduce (fn [s [k v]]
	      (str s (name k) "=\"" (if (keyword? v) (name v) v) "\" "))
	    "" attrs)))

(defn emit-tag [tag]
  (str "<" (get-name tag) " "
       (emit-attrs (get-attrs tag))
       (if (seq (get-content tag))
	 (str ">" (apply str (map #(if (string? %) % (emit-tag %)) (get-content tag)))
	      "</" (get-name tag) ">")
	 "/>")))

(defn emit [& tags]
  (reduce #(str %1 (emit-tag %2)) "" tags))

