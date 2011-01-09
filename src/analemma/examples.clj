(ns analemma.examples
  (:use [analemma.xml :only [emit add-content]]
	analemma.svg))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ANALEMMA EXAMPLES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ex-group (svg
	       (svg {:x 50 :y 50}
		    (-> (group
			  (-> (line 10 10 85 10)
			      (style :stroke "#006600"))
			  (-> (rect 10 20 50 75)
			      (style :stroke "#006600" :fill "#006600"))
			  (-> (text {:x 10 :y 90} "Text grouped with shapes")
			      (style :stroke "#660000" :fill "660000")))
			(rotate 45 50 50)))))

;; http://tutorials.jenkov.com/svg/rect-element.html
(def ex-round-rects
     (svg
      (-> (rect 10 10 50 50 :rx 5 :ry 5)
	  (style :stroke "#006600" :fill "#00cc00"))
      (-> (rect 70 10 50 50 :rx 10 :ry 10)
	  (style :stroke "#006600" :fill "#00cc00"))
      (-> (rect 130 10 50 50 :rx 15 :ry 15)
	  (style :stroke "#006600" :fill "#00cc00"))))

(def ex-circle (svg
		(-> (circle 40 40 24)
		    (style :stroke "#006600" :fill "#00cc00"))))

(def ex-ellipse (svg
		 (-> (ellipse 40 40 30 15)
		     (style :stroke "#006600" :fill "#00cc00"))))

(def ex-tri (svg
	     (-> (polygon [0,0 30,0 15,30 0,0])
		 (style :stroke "#006600" :fill "#33cc33"))))

(def ex-oct (svg
	     (-> (polygon [50,05 100,5 125,30 125,80 100,105 50,105 25,80 25,30])
		 (style :stroke "#660000"
			:fill "#cc3333"
			:stroke-width 3))))

;; http://tutorials.jenkov.com/svg/path-element.html
(def ex-path (svg
	      (-> (path [:M [50,50]
			 :A [30,30 0 0,1 35,20]
			 :L [100,100]
			 :M [110,110]
			 :L [100,0]])
		  (style :stroke "#660000" :fill :none))))

(def ex-text (svg
	      (text {:x 20 :y 40} "Example SVG text 1")
	      (-> (line 10 40 150 40)
		  (style :stroke "#000000"))))

(def ex-text2 (svg
	       (-> (text {:x 20 :y 40} "Rotated SVG text")
		   (style :stroke :none :fill "#000000")
		   (rotate 30 20 40))))

(def ex-text3 (svg
	       (-> (text {:x 20 :y 40} "Styled SVG text")
		   (style :font-family "Arial"
			  :font-size 34
			  :stroke "#000000"
			  :fill "#00ff00"))))

(def ex-text4 (svg
	        (text {:x 20 :y 10}
		      (tspan "tspan line 1")
		      (tspan "tspan line 2")
		      (tspan "tspan line 3"))))

(def ex-text5 (svg
	        (text {:y 10}
		      (tspan {:x 0} "tspan line 1")
		      (tspan {:x 0 :dy 15} "tspan line 2")
		      (tspan {:x 0 :dy 15} "tspan line 3"))))

(def ex-tref (svg
	      (defs [:the-text (text "A text that is referenced.")])
	       (text {:x 20 :y 10} (tref :the-text))
	       (text {:x 30 :y 30} (tref :the-text))))

(def ex-text-path (svg
		   (defs [:my-path (path [:M [75,20]
					 :a [1,1 0 0,0 100,0]])])
		   (-> (text {:x 10 :y 100}
			     (text-path "Text along a curved path..." :my-path))
		       (style :stroke "#000000"))))

(def ex-text-path2 (svg
		    (defs [:the-text (text "Text ref along a curved path...")
			   :my-path (path [:M [75,20]
					   :a [1,1 0 0,0 100,0]])])
		   (-> (text {:x 10 :y 100}
			     (text-path (tref :the-text) :my-path))
		       (style :stroke "#000000"))))

(def ex-img (svg
	     (-> (rect 10 10 130 500)
		 (style :fill "#000000"))
	     (image "http://jenkov.com/images/layout/top-bar-logo.png"
		    :x 20 :y 20 :width 300 :height 80)
	     (-> (line 25 80 350 80)
		 (style :stroke "#ffffff" :stroke-width 3))))

(def ex-trans (svg
	       (-> (rect 50 50 110 110)
		   (style :stroke "#ff0000" :fill "#ccccff")
		   (translate 30)
		   (rotate 45 50 50))
	       (-> (text {:x 70 :y 100} "Hello World")
		   (translate 30)
		   (rotate 45 50 50))))

(def ex-animate (svg
		 (-> (rect 10 10 110 110)
		     (style :stroke "#ff0000" :fill "#0000ff")
		     (animate-transform :begin 0
					:dur 20
					:type :rotate
					:from "0 60 60"
					:to "360 60 60"
					:repeatCount :indefinite))))

;; http://www.w3.org/TR/SVG/animate.html
(def ex-anim2 (svg
	       (-> (rect 1 1 298 798)
		   (style :fill "none" :stroke "blue" :stroke-width 2))

	       (-> (rect 300 100 300 100)
		   (style :fill (rgb 255 255 0))
		   (animate :x :begin 0 :dur 9 :from 300 :to 0)
		   (animate :y :begin 0 :dur 9 :from 100 :to 0)
		   (animate :width :begin 0 :dur 9 :from 300 :to 800)
		   (animate :height :begin 0 :dur 9 :from 100 :to 300))

	       (-> (group
		     (-> (text "It's alive")
			 (style :font-family :Verdana
				:font-size 35.27
				:visibility :hidden)
			 (animate :visibility :to :visible :begin 3)
			 (animate-motion :path (draw :M [0 0] :L [100 100])
					 :begin 3 :dur 6)
			 (animate-color :fill :from (rgb 0 0 255) :to (rgb 128 0 0)
					:begin 3 :dur 6)
			 (animate-transform :type :rotate :from -30 :to 0 :begin 3 :dur 6)
			 (animate-transform :type :scale :from 1 :to 3 :additive :sum
					    :begin 3 :dur 6)))
		   (translate 100 100))))

(def ex-anim-logo (svg
		   (-> (image "http://clojure.org/space/showimage/clojure-icon.gif"
			      :width 100 :height 100)
		       (animate :x :begin 0 :dur 9 :from 0 :to 300)
		       (animate :y :begin 0 :dur 9 :from 0 :to 300))))

(def ex-anim-logo2 (svg
		   (-> (image "http://clojure.org/space/showimage/clojure-icon.gif"
			      :width 100 :height 100)
		       (animate-transform :type :rotate :from -30 :to 0 :begin 0 :dur 6))))

(def ex-anim-logo3 (svg
		    (-> (group
			 (-> (image "http://clojure.org/space/showimage/clojure-icon.gif"
				    :width 100 :height 100)
			     (animate-transform :type :rotate
						:begin 0
						:dur 20
						:from "0 50 50"
						:to "360 50 50"
						:repeatCount :indefinite)))
			
		       (translate 100 100))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
		(format "%.0f" (translate-value (* i grid-x-space)
						0 width minx maxx)))
	  (style :fill (rgb 150 150 150)
		 :font-family "Verdana"
		 :font-size "12px"
		 :text-anchor "middle")))))

(defn y-axis [{:keys [n height width miny maxy]}]
  (let [grid-y-space (/ height n)]
    (for [i (range 1 (inc n)) :when (even? i)]
      (-> (text {:x 0 :y (- height (* i grid-y-space))}
		(format "%.0f" (translate-value (* i grid-y-space)
						0 height miny maxy)))
	  (style :fill (rgb 150 150 150)
		 :font-family "Verdana"
		 :font-size "12px"
		 :text-anchor "end"
		 :alignment-baseline "middle")))))

(defn xy-plot [& options]
  (let [grid (merge {:x 25, :y 25, :height 500, :width 750,
		     :minx 0, :maxx 100, :miny 0, :maxy 100,
		     :n 10, :points []}
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
	x* (translate-value x minx maxx 0 width)
	y* (- height (translate-value y miny maxy 0 height))
	label? (or (:label? options) false)
	point (apply circle x* y* r options)
	label (-> (text {:x (+ x* r) :y (- y* r)}
			(str (format "%.1f" (float x)) ","
			     (format "%.1f" (float y))))
		  (style :fill (rgb 255 255 255)
			 :font-family "Verdana"
			 :font-size "10px"))]
    (-> grid
	(update-in [:points] (fn [old] (conj old (apply assoc {:x x, :y y, :r r} options))))
	(assoc :svg (concat (:svg grid) (if label? [point label] [point]))))))

(defn add-points [grid x y & {:keys [sizes colors]}]
  (let [sizes (or sizes (repeat (count x) 3))
	colors (or colors (repeat (count x) (rgb 0 0 255)))
	data (map (fn [x y r color] [x y r color]) x y sizes colors)]
    (reduce (fn [svg [x y r color]] (add-point svg x y r :fill color))
	    grid data)))

(defn emit-grid [grid]
  (emit (svg (:svg grid))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; BASIC EXAMPLES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ex-test-grid []
  (let [grid (xy-plot)
	x (for [_ (range 25)] (rand-int 100))
	y (for [_ (range 25)] (rand-int 100))]
    (add-points grid x y)))

(defn ex-neg-grid []
  (let [grid (xy-plot :minx -50 :maxx 50 :miny -50 :maxy 50)
	x (for [_ (range 25)] (- (rand-int 100) 50))
	y (for [_ (range 25)] (- (rand-int 100) 50))]
    (add-points grid x y)))

(defn ex-sin-grid []
  (let [x (range 0 10 0.01)
	cos-points (map (fn [x] (+ 2.5 (Math/cos x))) x)
	sin-points (map (fn [x] (+ 2.5 (Math/sin x))) x)]
    (-> (xy-plot :maxx 10, :maxy 5)
	(add-points x cos-points)
	(add-points x sin-points :colors (repeat (count sin-points) (rgb 255 0 0))))))

(defn demo []
  (spit "/Users/liebke/Desktop/example.svg" (emit-grid (ex-test-grid))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ANALEMMA CHART
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; http://www.wsanford.com/~wsanford/exo/sundials/analemma_calc.html
(def analemma-data
     [[-15.165	-23.07]
      [-17.016	-22.70]
      [-19.171	-22.08]
      [-21.099	-21.27]
      [-22.755	-20.30]
      [-24.107	-19.16]
      [-25.446	-17.33]
      [-25.914	-16.17]
      [-26.198	-14.62]
      [-26.158	-12.96]
      [-25.814	-11.21]
      [-25.194	-9.39]
      [-24.520	-7.89]
      [-23.708	-6.37]
      [-22.529	-4.42]
      [-21.205	-2.45]
      [-19.777	-0.48]
      [-18.289	1.50]
      [-16.185	4.24]
      [-15.009	5.78]
      [-13.605	7.66]
      [-12.309	9.49]
      [-11.153	11.26]
      [-10.169	12.94]
      [-9.250	14.85]
      [-8.811	16.04]
      [-8.469	17.43]
      [-8.364	18.69]
      [-8.493	19.83]
      [-8.847	20.82]
      [-9.685	21.96]
      [-10.317	22.47]
      [-11.231	22.96]
      [-12.243	23.28]
      [-13.308	23.43]
      [-14.378	23.41]
      [-15.599	23.16]
      [-16.339	22.86]
      [-17.139	22.33]
      [-17.767	21.64]
      [-18.191	20.80]
      [-18.387	19.81]
      [-18.253	18.20]
      [-17.956	17.17]
      [-17.361	15.78]
      [-16.529	14.28]
      [-15.474	12.68]
      [-14.221	11.01]
      [-12.183	8.54]
      [-10.901	7.07]
      [-9.212	5.20]
      [-7.462	3.29]
      [-5.693	1.36]
      [-3.946	-0.59]
      [-1.938	-2.93]
      [-0.686	-4.48]
      [0.742	-6.39]
      [1.982	-8.28]
      [2.993	-10.11]
      [3.742	-11.88]
      [4.290	-14.23]
      [4.318	-15.49]
      [4.044	-16.97]
      [3.420	-18.33]
      [2.446	-19.55]
      [1.135	-20.63]
      [-0.852	-21.71]
      [-2.398	-22.29]
      [-4.538	-22.86]
      [-6.855	-23.24]
      [-9.286	-23.42]
      [-11.761	-23.41]
      [-14.691	-23.14]])

(defn analemma []
  (let [x (map first analemma-data)
	y (map second analemma-data)]
    (-> (xy-plot :minx -30 :maxx 10, :miny -30 :maxy 30 :height 500 :width 500)
	(add-points x y))))

(defn analemma-svg []
  (spit "/Users/liebke/Desktop/analemma.svg" (emit-grid (analemma))))