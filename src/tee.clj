(ns tee
  ^{:author "Paul deGrandis"
    :description "A clojure port of Zach Beane's macro that uses promises (http://xach.livejournal.com/268369.html)"}
  (:require [clojure.contrib.duck-streams :as duck-streams]))


;; Please see tee/jobs.clj and tee/distributed.clj for actual functionality

(defmacro tee
  "A macro that gathers data and prints progress as it writes results to a file
  NOTE: I never finished this and soon got distracted"
  [file & opts-and-body]
  `(let [body# (last ~opts-and-body)
         {:keys [overwrite#] :or {overwrite false}} (zipmap (butlast ~opts-and-body) (repeat true))]
    (if overwrite#
      (duck-streams/spit ~file @body#)
      (duck-streams/spit-append ~file @body#))))

