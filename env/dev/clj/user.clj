(ns user
  (:require [git-app.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [git-app.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'git-app.core/repl-server))

(defn stop []
  (mount/stop-except #'git-app.core/repl-server))

(defn restart []
  (stop)
  (start))


