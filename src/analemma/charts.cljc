(ns analemma.charts
  (:require [analemma.svg :refer [rect style line text group
                                  translate circle rgb svg
                                  translate-value]]
            [analemma.xml :refer [emit]]
            #?@(:cljs [[goog.string :as gstring]
                       [goog.string.format]])))

(def default-chart-props {:x 50, :y 50,
			  :height 500, :width 750,
			  :xmin 0, :xmax 100,
			  :ymin 0, :ymax 100,
			  :grid-lines 10,
			  :label-points?? false,
			  :points []
			  :major-grid-color (rgb 255 255 255)
			  :minor-grid-color (rgb 245 245 245)
			  :background-color (rgb 225 225 225)
			  :major-grid-width 2
			  :minor-grid-width 1
			  :axis-font-family "Verdana"
			  :axis-font-size "12px"
			  :axis-number-format "%.1f"
			  :label-font-family "Verdana"
			  :label-font-size "10px"
			  :label-number-format "%.1f"})

(defn chart-background [{:keys [height width background-color
				major-grid-color major-grid-width]}]
  (-> (rect 0 0 height width)
      (style :fill background-color
	     :stroke major-grid-color
	     :stroke-width major-grid-width)))

(defn x-grid [{:keys [height width
		      grid-lines
		      major-grid-color minor-grid-color
		      major-grid-width minor-grid-width]}]
  (let [grid-x-space (/ width grid-lines)]
    (for [i (range 1 grid-lines)]
      (-> (line (* i grid-x-space) 0 (* i grid-x-space) height)
	  (style :stroke (if (even? i) major-grid-color minor-grid-color)
		 :stroke-width (if (even? i) major-grid-width minor-grid-width))))))

(defn y-grid [{:keys [height width
		      grid-lines
		      major-grid-color minor-grid-color
		      major-grid-width minor-grid-width]}]
  (let [grid-y-space (/ height grid-lines)]
    (for [i (range 1 grid-lines)]
      (-> (line 0 (* i grid-y-space) width (* i grid-y-space))
	  (style :stroke (if (even? i) major-grid-color minor-grid-color)
		 :stroke-width (if (even? i) major-grid-width minor-grid-width))))))

(defn x-axis [{:keys [height width
		      xmin xmax
		      grid-lines
		      axis-font-family axis-font-size
		      axis-number-format]}]
  (let [grid-x-space (/ width grid-lines)]
    (for [i (range 0 (inc grid-lines)) :when (even? i)]
      (-> (text {:x (* i grid-x-space) :y (+ 20 height)}
		(#?(:clj format :cljs gstring/format) axis-number-format
                                                      (translate-value (* i grid-x-space)
                                                                       0 width xmin xmax)))
	  (style :fill (rgb 150 150 150)
		 :font-family axis-font-family
		 :font-size axis-font-size
		 :text-anchor :middle)))))

(defn y-axis [{:keys [height
		      ymin ymax
		      grid-lines
		      axis-font-family axis-font-size
		      axis-number-format]}]
  (let [grid-y-space (/ height grid-lines)]
    (for [i (range 1 (inc grid-lines)) :when (even? i)]
      (-> (text {:x 0 :y (- height (* i grid-y-space))}
		(#?(:clj format :cljs gstring/format) axis-number-format
                                                      (translate-value (* i grid-y-space)
                                                                       0 height ymin ymax)))
	  (style :fill (rgb 150 150 150)
		 :font-family axis-font-family
		 :font-size axis-font-size
		 :text-anchor :end
		 :alignment-baseline :middle)))))

(defn xy-plot [& options]
  (let [props (merge default-chart-props (apply hash-map options))]
    {:properties props
     :svg (-> (group (chart-background props))
	      (translate (:x props) (:y props))
	      (concat
	       (x-grid props)
	       (y-grid props)
	       (x-axis props)
	       (y-axis props)))}))

(defn point-label [{:keys [label-font-family
			   label-font-size
			   label-number-format]}
		   x* y* x y r & _]
  (-> (text {:x (+ x* r) :y (- y* r)}
	    (str (#?(:clj format :cljs gstring/format) label-number-format (float x)) ","
		 (#?(:clj format :cljs gstring/format) label-number-format (float y))))
      (style :fill (rgb 100 100 150)
	     :font-family label-font-family
	     :font-size label-font-size)))

(defn add-point [chart x y r & options]
  (let [props (:properties chart)
	{:keys [height width
		xmin xmax
		ymin ymax
		label-points?]} props
	x* (translate-value x xmin xmax 0 width)
	y* (- height (translate-value y ymin ymax 0 height))
	point (apply circle x* y* r options)
	label (point-label props x* y* x y r)]
    (-> chart
	(update-in [:points] (fn [old] (conj old (apply assoc {:x x, :y y, :r r} options))))
	(assoc :svg (concat (:svg chart) (if label-points? [point label] [point]))))))

(defn points->xy [points]
  (reduce (fn [[x y] [p1 p2]] [(conj x p1) (conj y p2)])
	  [[] []] points))

(defn xy->points [[x y]]
  (map (fn [p1 p2] [p1 p2]) x y))

(defn add-points [chart data & {:keys [size sizes colors transpose-data?? fill]}]
  (let [[x y] (if transpose-data?? data (points->xy data))
	sizes (or sizes (repeat (count x) (or size 3)))
	colors (or colors (repeat (count x) (or fill (rgb 0 0 255))))
	data (map (fn [x y r color] [x y r color]) x y sizes colors)]
    (reduce (fn [svg [x y r color]] (add-point svg x y r :fill color))
	    chart data)))

(defn emit-svg [chart]
  (emit (svg (:svg chart))))
