(ns git-app.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [git-app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[git-app started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[git-app has shut down successfully]=-"))
   :middleware wrap-dev})
