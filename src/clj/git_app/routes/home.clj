(ns git-app.routes.home
  (:require [git-app.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [ring.util.response :refer [redirect]]
            [clojure.java.shell :as shell]
            [selmer.parser :refer [render-file]]
            [clojure.data.json :as json]
            [git-app.automate-functions :as automate-functions]
            [git-app.git-functions :as git-functions]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

; (defn git-function [data]
;   (println data)
;   (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; git pull origin master; lein uberjar")
;   ;(println (:out (shell/sh "sh" "lein eastwood")))
;   (println "Done!!!!")

;   (render-file "charts_empty.html" {:message (:out (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; lein eastwood"))}))

; (defn run-lein-test []
;   (.contains (get (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; lein test") :out) "Tests failed."))

; (defn run-lein-eastwood []
;   (.contains (get (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; lein eastwood") :out) "Subprocess failed"))

; (defn create-github-issue []
;   ;(println "Creating Github Issue!!!!!")
;   ; (shell/sh "curl --user \"sanjay-rajeswaran:166d964ccab418464279b6f2c58d12e9e63cda38\" --request POST --data '{\"title\":\"Automated Error Report\", \"body\":\"Tests Failed!!!!!\"}' https://api.github.com/repos/sanjay-rajeswaran/twitter-project-clojure/issues"))
;   (shell/sh "curl" "-u" "sanjay-rajeswaran:698050c9df70a583098d2fe4a457cd35afaa7006" "--request" "POST" "--data" "{\"title\":\"Automated Error Report\", \"body\":\"Tests Failed!!!!!\"}" "https://api.github.com/repos/sanjay-rajeswaran/twitter-project-clojure/issues")
;   (println "Created Github Issue"))

; (defn create-jar []
;   ; create a jar from the sourcecode
;   (println "Entering Jar Flow!!!")
;   (shell/sh "sh" "-c" "cd /Users/sanjay_r/Work/twitter-port; lein uberjar")
;   )

(defn automate [payload]
  (println "Entering Git Flow!!!")
  (let [branch_name (get-in payload [:pull_request :head :ref])]
    ; pull latest code from repo master branch
    (shell/sh "sh" "-c" (str "cd /Users/sanjay_r/Work/twitter-port; git checkout " branch_name "; git pull origin "
      branch_name)))

  ; run checks and automate
  (println "Entering Checks Flow!!!")
  (if-not (automate-functions/run-lein-eastwood)
    (if-not (automate-functions/run-lein-test)
      (automate-functions/create-jar payload)
      ;(git-functions/post-pullrequest-comment (get-in payload [:pull_request :issue_url])))
      (git-functions/post-status (get-in payload [:pull_request :statuses_url]) "failure" "Initial build tests failed."))
    (git-functions/post-status (get-in payload [:pull_request :statuses_url]) "failure" "Initial build tests failed.")
    ;(git-functions/post-pullrequest-comment (get-in payload [:pull_request :issue_url]))
  ))

(defn git-post-pullrequest [data]

  ;(println (data :payload))

  (let [payload (json/read-str (data :payload) :key-fn keyword)]
    (if (= (payload :action) "opened")
      (automate payload)
      (println "Not a new pull request.")))
  
  (println "############### Done ###############")

  {:status "200"})

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/git" {params :params} (git-post-pullrequest params))
  (GET "/about" [] (about-page))
  ;(GET "/github" [data] (git-function data))
  ;(POST "/git" [action] (git-post-push [action]))
  ;(POST "/git" [] :form-params [username :- String,
  ;   password :- String] (git-post-push))
  )
