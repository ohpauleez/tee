(defproject org.clojars.ohpauleez/tee "0.1.0-SNAPSHOT"
  :description "A clojure port of Zach Beane's macro that uses promises (http://xach.livejournal.com/268369.html)"
  :url "http://github.com/ohpauleez/tee"
  :dependencies [[org.clojure/clojure "1.3.0-master-SNAPSHOT"]
                 [org.clojure.contrib/duck-streams "1.3.0-SNAPSHOT"]
                 [aleph "0.1.1-SNAPSHOT"]]
  :dev-dependencies [[swank-clojure "1.3.0-SNAPSHOT"]
                     ;[lein-nailgun "0.1.0"]
                     ;[vimclojure "2.2.0-SNAPSHOT"]
                     [vimclojure/server "2.2.0-SNAPSHOT"]
                     [org.clojure.contrib/repl-utils "1.3.0-SNAPSHOT"]]) ; there's a bug in the 'show' function
                     ;[lein-run "1.0.0-SNAPSHOT"]
                     ;[lein-difftest "1.2.3"]
                     ;[radagast "1.0.0"]])

