(ns analemma.xml
  (:require [clojure.xml :as xml])
  (:import [java.io ByteArrayInputStream]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; XML FUNCTIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn has-attrs? [tag]
  (map? (second tag)))

(defn has-content? [tag]
  (if (has-attrs? tag)
    (> (count tag) 2)
    (> (count tag) 1)))

(defn get-name [tag]
  (if-let [n (first tag)]
    (name n)))

(defn get-attrs [tag]
  (if (has-attrs? tag) (second tag) {}))

(defn get-content [tag]
  (if (has-attrs? tag)
    (drop 2 tag)
    (rest tag)))

(defn set-attrs [tag & attrs]
  (concat [(get-name tag)
           (apply hash-map attrs)]
	  (get-content tag)))

(defn set-content [tag & content]
  (concat [(get-name tag) (get-attrs tag)]
	  content))

(defn add-attrs [tag & attrs]
  (concat [(get-name tag)
	   (apply assoc (get-attrs tag) attrs)]
	  (get-content tag)))

(defn merge-attrs [tag attrs]
  (concat [(get-name tag)
	   (merge (get-attrs tag) attrs)]
	  (get-content tag)))

(defn add-content [tag & content]
  (concat [(get-name tag)
	   (get-attrs tag)]
	  (concat (get-content tag)
		  content)))

(defn update-attrs [tag [& keys] update-fn & args]
  (set-attrs tag (apply update-in (get-attrs tag) keys update-fn args)))

(defn emit-attrs [attrs]
  (when attrs
    (reduce (fn [s [k v]]
	      (str s (name k) "=\"" (if (keyword? v) (name v) v) "\" "))
	    "" attrs)))

(defn emit-tag [tag]
  (if-let [n (get-name tag)]
    (str "<" n " "
	(emit-attrs (get-attrs tag))
	(if (seq (get-content tag))
	  (str ">" (apply str (map #(if (string? %) % (emit-tag %)) (get-content tag)))
	       "</" n ">")
	  "/>"))))

(defn emit [& tags]
  (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
       (reduce #(str %1 (emit-tag %2)) "" tags)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FUNCTIONS FOR PARSING XML FILES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-xml-map [xml-string]
  (xml/parse (ByteArrayInputStream. (.getBytes xml-string "UTF-8"))))

(defn parse-xml-map [{:keys [tag attrs content]}]
  (lazy-cat (if attrs [tag attrs] [tag])
	    (map parse-xml-map content)))

(defn parse-xml [xml-string]
  (parse-xml-map (get-xml-map xml-string)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FUNCTIONS FOR QUERYING PARSED XML
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn query-children [xml-vec query-type query]
  (let [pred (fn [child]
	       (condp = query-type
		   :tag (= (first child) query)   
		   :attrs (reduce (fn [bool [k v]]
				   (and bool (= (get (get-attrs child) k) v)))
				 true query)))]
    (filter pred (get-content xml-vec))))

(defn query-descendents [xml-vec [& query-maps]]
  "Example:
    (query-descendents xml-vec [{:tag val1} {:attrs {name1 val1, name2 val2}} {:tag val2}])
  "
  (loop [results [xml-vec]
	 [query & next-queries] query-maps]
    (if query
      (recur (mapcat #(query-children % (key (first query)) (val (first query)))
		     results)
	     next-queries)
      results)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FUNCTIONS FOR TRANSFORMING PARSED XML
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn transform-children [xml-vec f query-type query]
  (let [parent (if (has-attrs? xml-vec)
		 [(first xml-vec) (get-attrs xml-vec)]
		 [(first xml-vec)])
	pred (fn [child]
	       (condp = query-type
		   :tag (= (first child) query)   
		   :attrs (reduce (fn [bool [k v]]
				   (and bool (= (get (get-attrs child) k) v)))
				 true query)))]
    (concat parent (map #(if (pred %) (f %) %) (get-content xml-vec)))))

(defn transform-descendents [xml-vec f [& query-maps]]
  "Example:
    (transform-descendents xml-vec #(add-attrs % :fill \"#ff0000\")
                           [{:tag val1} {:attrs {name1 val1, name2 val2}} {:tag val2}])
  "
  (loop [results [xml-vec]
	 [query & next-queries] query-maps]
    (if query
      (recur (mapcat #(transform-children % f (key (first query)) (val (first query)))
		     results)
	     next-queries)
      results)))


