(ns examples.charts
  (:use [analemma.charts :only [emit-svg xy-plot add-points]]
	[analemma.svg :only [rgb]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; BASIC CHART EXAMPLES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn rand-plot [directory]
  (let [x (repeatedly 25 #(rand-int 100))
	y (repeatedly 25 #(rand-int 100))]
    (spit (str directory "/rand-plot.svg")
	  (emit-svg
	   (-> (xy-plot :width 500 :height 500 :label? true)
	       (add-points [x y] :transpose? true))))))

(defn sin-cos-plot [directory]
  (let [x (range -5 5 0.05)
	y1 (map #(Math/cos %) x)
	y2 (map #(Math/sin %) x)]
    (spit (str directory "/sin-cos-small.svg")
	  (emit-svg
	    (-> (xy-plot :width 450 :height 200
			 :xmin -5 :xmax 5
			 :ymin -1.5 :ymax 1.5)
		(add-points [x y1] :transpose? true
			    :size 1)
		(add-points [x y2] :transpose? true
			    :size 1
			    :fill (rgb 255 0 0)))))))


