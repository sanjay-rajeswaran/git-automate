(ns git-app.git-functions
  (:require [clojure.java.shell :as shell]
  	[clj-http.client :as client]
  	))

(defn create-github-issue []
  ;(println "Creating Github Issue!!!!!")
  ; (shell/sh "curl --user \"sanjay-rajeswaran:166d964ccab418464279b6f2c58d12e9e63cda38\" --request POST --data '{\"title\":\"Automated Error Report\", \"body\":\"Tests Failed!!!!!\"}' https://api.github.com/repos/sanjay-rajeswaran/twitter-project-clojure/issues"))
  (shell/sh "curl" "-u" "sanjay-rajeswaran:698050c9df70a583098d2fe4a457cd35afaa7006" "--request" "POST" "--data" "{\"title\":\"Automated Error Report\", \"body\":\"Tests Failed!!!!!\"}" "https://api.github.com/repos/sanjay-rajeswaran/twitter-project-clojure/issues")
  (println "Created a Github Issue"))

(defn post-pullrequest-comment [issue_url]
  (shell/sh "curl" "-u" "sanjay-rajeswaran:698050c9df70a583098d2fe4a457cd35afaa7006" "--request" "POST" "--data" "{\"body\":\"Initial Tests Failed!!!!!\"}" (str issue_url "/comments"))
  (println "Added a Pull Request Commit"))

(defn post-status [status_url test_status description]
	(println (client/post status_url {:basic-auth ["sanjay-rajeswaran" "698050c9df70a583098d2fe4a457cd35afaa7006"] :form-params {:state test_status :description description :context "continuous-integrtions/git-app"} :content-type :json})))