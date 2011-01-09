(ns examples.svg
  (:use [analemma.xml :only [emit add-content]]
	analemma.svg))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ANALEMMA SVG EXAMPLES
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



