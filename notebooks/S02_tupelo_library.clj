(ns S02-tupelo-library
  (:require [nextjournal.clerk :as clerk]))





(clerk/table {:head ["baslık-1" "baslık-2" "baslık-2"]
             :rows [["1" "asd" "3"] [2 "dfg" 4]]})


(clerk/html [:table
             [:tr [:th "First_Name"] [:th "Last_Name"] [:th "Marks"]]
             [:tr [:td "Sonoo"] [:td "Jaiswal"] [:td "60"]]
             ])
