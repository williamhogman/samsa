(ns samsa.core
  (:use [com.rpl.specter]
        [flatland.protobuf.core :as pb])
  (:import se.zensum.webhook.PayloadOuterClass
           (flatland.protobuf PersistentProtocolBufferMap PersistentProtocolBufferMap$Def PersistentProtocolBufferMap$Def$NamingStrategy Extensions)
           (com.google.protobuf GeneratedMessage CodedInputStream Descriptors$Descriptor)))

;; Hacky function to create protobuf's that works w/ current Clojure
(defn mkpbdef [descriptor]
  (let [naming-strategy PersistentProtocolBufferMap$Def/convertUnderscores
        size-limit 67108864]
    (PersistentProtocolBufferMap$Def/create descriptor naming-strategy size-limit)))

(def Payload (mkpbdef (se.zensum.webhook.PayloadOuterClass$Payload/getDescriptor)))

(defnav BYTES-AS-PROTOBUF [type]
  (select* [this structure next-fn]
           (next-fn (pb/protobuf-load type structure)))
  (transform* [this structure next-fn]
              (let [w (pb/protobuf-load type structure)
                    conv (next-fn w)]
                (pb/protobuf-dump conv))))


(comment
  (do
    ;; Create a payload and dump it
    (def buf-dump (pb/protobuf-dump (pb/protobuf Payload :body "Foo")))

    ;; Access the bytes of the protobuf, and append bar to the end of it
    (def bb
      (setval [(BYTES-AS-PROTOBUF Payload) :body END] "bar" buf-dump))
    ;; Then read the updated bytes as a payload
    (pb/protobuf-load Payload bb)))
