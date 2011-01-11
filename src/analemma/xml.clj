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
  (name (first tag)))

(defn get-attrs [tag]
  (if (has-attrs? tag) (second tag) {}))

(defn get-content [tag]
  (if (has-attrs? tag)
    (drop 2 tag)
    (rest tag)))

(defn set-attrs [tag & attrs]
  (concat [(get-name tag) attrs]
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
  (str "<" (get-name tag) " "
       (emit-attrs (get-attrs tag))
       (if (seq (get-content tag))
	 (str ">" (apply str (map #(if (string? %) % (emit-tag %)) (get-content tag)))
	      "</" (get-name tag) ">")
	 "/>")))

(defn emit [& tags]
  (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
       (reduce #(str %1 (emit-tag %2)) "" tags)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FUNCTIONS FOR PARSING XML FILES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parse-xml-map [xml-map]
  (let [f (fn [v m]
	    (let [{:keys [tag attrs content]} m
		  attrs-add #(if attrs (conj % attrs) %)
		  content-add #(if content
				 (apply conj % (map parse-xml-map content))
				 %)]
	      (apply conj v (content-add (attrs-add [tag])))))]
    (reduce f [] [xml-map])))

(defn parse-xml [xml-string]
  (parse-xml-map (xml/parse (ByteArrayInputStream. (.getBytes xml-string "UTF-8")))))