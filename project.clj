(defproject tonos-client "1.18.0-SNAPSHOT"
  :description "Clojure bindings to TON OS SDK's Core Client Library"
  :url "https://github.com/serge-medvedev/tonos-client-clojure"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[net.java.dev.jna/jna "5.6.0"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/core.async "1.3.610"]]
  :source-paths ["src" "target/generated"]
  :aot [tonos.client.handlers]
  :java-source-paths ["java"]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.1"]
                                  [cljstache "2.0.6"]]
                   :source-paths ["src" "dev-src" "target/generated"]
                   :resource-paths ["dev-rc"]}
             :gen {:prep-tasks [["with-profile" "dev" "run" "-m" "tonos.client.codegen/generate"] "javac" "compile"]}}
  :test-selectors {:slow :slow
                   :fast (complement :slow)
                   :paid :paid
                   :free (complement :paid)
                   :default (constantly true)}
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    :sign-releases false}]]
  :aliases {"gen" ["with-profile" "dev" "run" "-m" "tonos.client.codegen/generate"]
            "test" ["with-profile" "dev,gen" "test"]
            "install" ["with-profile" "gen" "install"]
            "deploy" ["with-profile" "gen" "deploy"]}
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["changelog" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["with-profile" "gen" "deploy"]
                  ["vcs" "push"]])
