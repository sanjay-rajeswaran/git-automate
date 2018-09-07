(ns git-app.git-functions
  (:require [clojure.java.shell :as shell]
  	[clj-http.client :as client]
  	))

(defn create-github-issue []
  ;(shell/sh "curl" "-u" "sanjay-rajeswaran:698050c9df70a583098d2fe4a457cd35afaa7006" "--request" "POST" "--data" "{\"title\":\"Automated Error Report\", \"body\":\"Tests Failed!!!!!\"}" "https://api.github.com/repos/sanjay-rajeswaran/twitter-project-clojure/issues")
  (client/post "https://api.github.com/repos/sanjay-rajeswaran/twitter-project-clojure/issues"
  	; replace the git access token
  	{:basic-auth ["sanjay-rajeswaran" "698050c9df70a583098d2fe4a457cd35afaa7006"] :form-params {:body "Initial Tests Failed!!!!!"} :content-type :json})
  (println "Created a Github Issue"))

(defn post-pullrequest-comment [issue_url]
  ;(shell/sh "curl" "-u" "sanjay-rajeswaran:698050c9df70a583098d2fe4a457cd35afaa7006" "--request" "POST" "--data" "{\"body\":\"Initial Tests Failed!!!!!\"}" (str issue_url "/comments"))
  (client/post (str issue_url "/comments") {:basic-auth ["sanjay-rajeswaran" "698050c9df70a583098d2fe4a457cd35afaa7006"] :form-params {:body "Initial Tests Failed!!!!!"} :content-type :json})
  (println "Added a Pull Request Commit"))

(defn post-status [status_url test_status description]
	(client/post status_url {:basic-auth ["sanjay-rajeswaran" "698050c9df70a583098d2fe4a457cd35afaa7006"] :form-params {:state test_status :description description :context "continuous-integrtions/git-app"} :content-type :json}))