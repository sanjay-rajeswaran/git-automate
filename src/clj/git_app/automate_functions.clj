(ns git-app.automate-functions
  (:require [clojure.java.shell :as shell]
  	[git-app.git-functions :as git-functions]))

(defn run-lein-test []
  (println "Running Clojure Unit Tests")
  (.contains (get (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; lein test") :out) "Tests failed."))

(defn run-lein-eastwood []
  (println "Running lint checks")
  (.contains (get (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; lein eastwood") :out) "Subprocess failed")
  ;(println "Skipping lint checks for now!")
  )

(defn create-jar [payload]

  ; post success status on pullrequest
  (git-functions/post-status (get-in payload [:pull_request :statuses_url]) "success" "Initial build tests passed.")

  ; create a jar from the sourcecode
  (println "All checks Passed!!!\nEntering Jar Flow!!!")
  (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; lein uberjar")
  (println "Jar Created!"))