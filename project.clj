(defproject ton-client "1.0.0-rc"
  :description "Clojure bindings to TON SDK's Core Client Library"
  :url "https://github.com/serge-medvedev/ton-client-clojure"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[net.java.dev.jna/jna "5.6.0"]
                 [cheshire "5.10.0"]
                 [org.clojure/core.async "1.3.610"]]
  :aot [ton.client.handlers]
  :java-source-paths ["java"]
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.0"]]}}
  :deploy-repositories [["releases" :clojars]]
  :aliases {"update-readme-version" ["shell" "sed" "-i" "s/\\\\[ton-client \"[0-9.]*\"\\\\]/[ton-client \"${:version}\"]/" "README.md"]}
  :release-tasks [["shell" "git" "diff" "--exit-code"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["changelog" "release"]
                  ["update-readme-version"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["vcs" "push"]])
