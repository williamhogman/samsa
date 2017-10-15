(set-env! :repositories #(conj % ["jitpack" "https://jitpack.io"]))
(set-env! :repositories #(conj % ["clojars" "http://clojars.org/repo"]))
(set-env!
 :resource-paths #{"src"}
 :dependencies '[[com.rpl/specter "1.0.3"]
                 [org.flatland/protobuf "0.8.2-SNAPSHOT"]
                 [com.github.zensum/webhook-proto "0.1.1"]])
