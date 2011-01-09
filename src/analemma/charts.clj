(ns analemma.charts
  (:use [analemma.svg :only [rect style line text
			     group translate circle
			     rgb svg]]
	[analemma.xml :only [emit]]))

(defn translate-value [v from-min from-max to-min to-max]
  (let [scale (/ (- to-max to-min)
		 (- from-max from-min))
	trans (- to-min (* from-min scale))]
    (float (+ (* v scale) trans))))

(defn grid-background [{:keys [height width]} fill-color major-color major-width]
  (-> (rect 0 0 height width)
      (style :fill fill-color
	     :stroke major-color
	     :stroke-width major-width)))

(defn x-grid [{:keys [n height width minx maxx]}
	      major-color minor-color major-width minor-width]
  (let [grid-x-space (/ width n)]
    (for [i (range 1 n)]
      (-> (line (* i grid-x-space) 0 (* i grid-x-space) height)
	  (style :stroke (if (even? i) major-color minor-color)
		 :stroke-width (if (even? i) major-width minor-width))))))

(defn y-grid [{:keys [n height width miny maxy]}
	      major-color minor-color major-width minor-width]
  (let [grid-y-space (/ height n)]
    (for [i (range 1 n)]
      (-> (line 0 (* i grid-y-space) width (* i grid-y-space))
	  (style :stroke (if (even? i) major-color minor-color)
		 :stroke-width (if (even? i) major-width minor-width))))))

(defn x-axis [{:keys [n height width minx maxx]}]
  (let [grid-x-space (/ width n)]
    (for [i (range 0 (inc n)) :when (even? i)]
      (-> (text {:x (* i grid-x-space) :y (+ 20 height)}
		(format "%.1f" (translate-value (* i grid-x-space)
						0 width minx maxx)))
	  (style :fill (rgb 150 150 150)
		 :font-family "Verdana"
		 :font-size "12px"
		 :text-anchor "middle")))))

(defn y-axis [{:keys [n height width miny maxy]}]
  (let [grid-y-space (/ height n)]
    (for [i (range 1 (inc n)) :when (even? i)]
      (-> (text {:x 0 :y (- height (* i grid-y-space))}
		(format "%.1f" (translate-value (* i grid-y-space)
						0 height miny maxy)))
	  (style :fill (rgb 150 150 150)
		 :font-family "Verdana"
		 :font-size "12px"
		 :text-anchor "end"
		 :alignment-baseline "middle")))))

(defn xy-plot [& options]
  (let [grid (merge {:x 50, :y 50, :height 500, :width 750,
		     :minx 0, :maxx 100, :miny 0, :maxy 100,
		     :n 10, :label? false, :points []}
		    (apply hash-map options))
	{:keys [x y height width minx maxx miny maxy n points]} grid
	 major-color (rgb 255 255 255)
	 minor-color (rgb 245 245 245)
	 grid-fill (rgb 225 225 225)
	 major-width 2
	 minor-width 1]
    {:spec grid
     :svg (-> (group 
	        (grid-background grid grid-fill major-color major-width))
	      (translate x y)
	      (concat
	       (x-grid grid major-color minor-color major-width minor-width)
	       (y-grid grid major-color minor-color major-width minor-width)
	       (x-axis grid)
	       (y-axis grid)))}))

(defn add-point [grid x y r & options]
  (let [{:keys [height width minx maxx miny maxy n]} (:spec grid)
	label? (-> grid :spec :label?)
	x* (translate-value x minx maxx 0 width)
	y* (- height (translate-value y miny maxy 0 height))
	point (apply circle x* y* r options)
	label (-> (text {:x (+ x* r) :y (- y* r)}
			(str (format "%.1f" (float x)) ","
			     (format "%.1f" (float y))))
		  (style :fill (rgb 100 100 150)
			 :font-family "Verdana"
			 :font-size "10px"))]
    (-> grid
	(update-in [:points] (fn [old] (conj old (apply assoc {:x x, :y y, :r r} options))))
	(assoc :svg (concat (:svg grid) (if label? [point label] [point]))))))

(defn points->xy [points]
  (reduce (fn [[x y] [p1 p2]] [(conj x p1) (conj y p2)])
	  [[] []] points))

(defn xy->points [[x y]]
  (map (fn [p1 p2] [p1 p2]) x y))

(defn add-points [grid data & {:keys [sizes colors transpose?]}]
  (let [[x y] (if transpose? data (points->xy data))
	sizes (or sizes (repeat (count x) 3))
	colors (or colors (repeat (count x) (rgb 0 0 255)))
	data (map (fn [x y r color] [x y r color]) x y sizes colors)]
    (reduce (fn [svg [x y r color]] (add-point svg x y r :fill color))
	    grid data)))

(defn emit-svg [chart]
  (emit (svg (:svg chart))))

