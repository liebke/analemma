(ns examples.charts
  (:use [analemma.charts :only [emit-svg xy-plot add-points]]
	[analemma.svg :only [rgb]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; BASIC CHART EXAMPLES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn example1 []
  (let [grid (xy-plot :label? true)
	x (for [_ (range 25)] (rand-int 100))
	y (for [_ (range 25)] (rand-int 100))]
    (add-points grid [x y] :transpose? true)))

(defn example2 []
  (let [x (range -5 5 0.05)
	y1 (map #(Math/cos %) x)
	y2 (map #(Math/sin %) x)]
    (-> (xy-plot :xmin -5 :xmax 5 :ymin -1.5 :ymax 1.5)
	(add-points [x y1] :transpose? true)
	(add-points [x y2] :transpose? true
		    :colors (repeat (count x) (rgb 255 0 0))))))

(defn demo []
  (spit "example1.svg" (emit-svg (example1)))
  (spit "example2.svg" (emit-svg (example2))))

