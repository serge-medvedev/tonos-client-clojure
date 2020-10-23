(defproject org.clojars.serge-medvedev/ton-client "1.0.0-rc"
  :description "Clojure bindings to TON SDK's Core Client Library"
  :url "https://github.com/serge-medvedev/ton-client-clojure"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[net.java.dev.jna/jna "5.6.0"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/core.async "1.3.610"]]
  :source-paths ["src" "target/generated"]
  :main ^:skip-aot ton.client.codegen
  :aot [ton.client.handlers]
  :java-source-paths ["java"]
  :prep-tasks [["with-profile" "+gen,+dev" "run"] "compile"]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.0"]
                                  [cljstache "2.0.6"]]
                   :source-paths ["src" "dev-src" "target/generated"]}
             :gen {:prep-tasks ^:replace []}}
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :deploy-repositories [["releases" :clojars]]
  :aliases {"update-readme-version" ["shell" "sed" "-i" "s/\\\\[ton-client \"[0-9.]*\"\\\\]/[ton-client \"${:version}\"]/" "README.md"]
            "gen" ["with-profile" "+gen,+dev" "run"]}
  :release-tasks [["shell" "git" "diff" "--exit-code"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["changelog" "release"]
                  ["update-readme-version"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["vcs" "push"]])
