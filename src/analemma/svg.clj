(ns analemma.svg
  (:require [analemma.xml :as xml]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SVG FUNCTIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn svg [& content]
  (let [xmlns {"xmlns" "http://www.w3.org/2000/svg"
	       "xmlns:xlink" "http://www.w3.org/1999/xlink"}
	attrs (if (map? (first content)) (first content) {})
	content (if (map? (first content)) (rest content) content)]
    (concat [:svg (merge xmlns attrs)] content)))

(defn style [elem & properties]
  (let [props (apply hash-map properties)
	styling (when (seq props)
		  (reduce (fn [s [k v]]
			    (str s " " (name k) ": " (if (keyword? v) (name v) v) "; "))
			  "" props))]
    (concat [(xml/get-name elem) (assoc (xml/get-attrs elem) :style styling)]
	    (xml/get-content elem))))

(defn line [x1 y1 x2 y2 & options]
  (let [attrs (apply hash-map options)]
    [:line (apply merge {:x1 x1, :y1 y1, :x2 x2, :y2 y2} attrs)]))

(defn rect [x y height width & options]
  (let [attrs (apply hash-map options)]
    [:rect (apply merge {:x x, :y y, :height height, :width width} attrs)]))

(defn circle [cx cy r & options]
  (let [attrs (apply hash-map options)]
    [:circle (apply merge {:cx cx, :cy cy, :r r} attrs)]))

(defn ellipse [cx cy rx ry & options]
  (let [attrs (apply hash-map options)]
    [:ellipse (apply merge {:cx cx, :cy cy, :rx rx, :ry ry} attrs)]))

(defn polygon [[& points] & options]
  (let [attrs (apply hash-map options)
	points (reduce (fn [s [x y]] (str s " " x "," y))
		       "" (partition 2 points))]
    [:polygon (apply merge {:points points}
		     attrs)]))

(defn text [& content]
  (concat [:text] content))

(defn group [& content]
  (cons :g content))

(defn draw [& commands]
  (reduce (fn [s [cmd args]] (str s " " (name cmd) (apply str (interpose "," args))))
	  "" (partition 2 commands)))

(defn path [draw-commands & options]
  (let [attrs (apply hash-map options)]
    [:path (merge {:d (apply draw draw-commands)} attrs)]))

(defn tref [id]
  [:tref {"xlink:href" (str "#" (name id))}])

(defn rgb [r g b]
  (str "rgb(" r "," g "," b ")"))

(defn animate [elem attr & attrs]
  (concat elem [[:animate (merge {:attributeName (name attr), :begin 0, :fill "freeze"}
				 (apply hash-map attrs))]]))

(defn animate-motion [elem & attrs]
  (concat elem [[:animateMotion (merge {:begin 0, :fill "freeze"}
				       (apply hash-map attrs))]]))

(defn animate-color [elem attr & attrs]
  (concat elem [[:animateColor (merge {:attributeName (name attr), :begin 0, :fill "freeze"}
				      (apply hash-map attrs))]]))

(defn animate-transform [elem & attrs]
  (concat elem [[:animateTransform (merge {:attributeName "transform",
					   :begin 0, :fill "freeze"}
					  (apply hash-map attrs))]]))

(defn transform [elem trans]
  (let [attrs (xml/get-attrs elem)
	trans (if (:transform attrs)
		(str (:transform attrs) " " trans)
		trans)]
    (concat [(xml/get-name elem)
	     (assoc attrs :transform trans)]
	    (xml/get-content elem))))

;; transform functions
(defn rotate [elem angle x y]
  (transform elem (str "rotate(" angle "," x "," y ")")))

(defn translate
  ([elem x] (transform elem (str "translate(" x ")")))
  ([elem x y] (transform elem (str "translate(" x "," y ")"))))

(defn defs [[& bindings]]
  (let [bindings (partition 2 bindings)
	f (fn [defs-tag [id tag]]
	    (conj defs-tag
		  (concat [(xml/get-name tag)
			   (assoc (xml/get-attrs tag) :id (name id))]
			  (xml/get-content tag))))]
    (reduce f [:defs] bindings)))

(defn text-path [text path-id]
  [:textPath {"xlink:href" (str "#" (name path-id))} text])

(defn tspan [& content]
  (concat [:tspan] content))

(defn image [href & options]
  (let [attrs (apply hash-map options)]
    [:image (merge {"xlink:href" href} attrs)]))
