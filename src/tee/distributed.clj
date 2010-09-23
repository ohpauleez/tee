(ns tee.distributed
  ^{:author "Paul deGrandis"
    :description "A clojure port of Zach Beane's macro that uses promises (http://xach.livejournal.com/268369.html)"}
  (:require [aleph.core.channel :as aleph]
            [aleph.tcp :as aleph-tcp])
  (:use [clojure.main :only (repl)])
  (:import [java.net Socket]
           [java.io InputStreamReader ByteArrayInputStream OutputStream OutputStreamWriter PrintWriter]
           [clojure.lang LineNumberingPushbackReader]))


;; TODO look into rolling in functionality or support for work (http://github.com/clj-sys/work)

;;;; DISTRIBUTED stuff
;; TODO add in count-me-in function, that registers a jobber with a server (allows for P2P jobbing)
(defn byte-buffer->str
  "NIO ByteBuffer to string"
  ^String [buf]
  (let  [b-array  (byte-array  (.capacity buf))
         updated-ins  (.get buf b-array 0  (alength b-array))
         s-buf (String. b-array)]
    s-buf))

(defn- socket-repl
  "Connects and binds channels and streams for "
  ;([^ByteBuffer ins] ; this works with nio/aleph
  ([ins] ; this works with nio/aleph
    (let [b-array (byte-array (.capacity ins))
          updated-ins (.get ins b-array 0 (alength b-array))] ;this call is destructive: updated-ins === ins
      (binding [*in* (LineNumberingPushbackReader. (InputStreamReader. 
                                                     (ByteArrayInputStream. b-array)))]
        (repl))))
  ([ins ch] ; this works with nio/aleph
    (let [b-array (byte-array (.capacity ins))
          updated-ins (.get ins b-array 0 (alength b-array))] ;this call is destructive: updated-ins === ins
      (binding [*in* (LineNumberingPushbackReader. (InputStreamReader. 
                                                     (ByteArrayInputStream. b-array)))]
        (repl :print (fn [res] (aleph/enqueue-and-close ch (str res))))))))

(defn print-handler
  "a simple handler for aleph that prints messages to stdout"
  [ch connection-info]
  (aleph/receive-all ch #(println %)))

(defn echo-handler
  "A simple echo server for aleph"
  [ch connection-info]
  ;(aleph/siphon ch ch))
  (receive-all channel #(aleph/enqueue ch %)))

(defn repl-handler
  "Aleph async repl server"
  [ch channel-info]
  (aleph/receive-all ch #(aleph/enqueue ch (socket-repl %)))) ;[ins]
  ;(aleph/receive-all ch #(socket-repl % ch))) ;[ins ch]

(defn eval-handler
  "Async eval server for aleph"
  [ch channel-info]
  (aleph/receive-all ch #(aleph/enqueue ch (str (load-string (heap-buffer->str %))))))

;(def server (aleph-tcp/start-tcp-server echo-handler {:port 1234}))
(def init-server #(aleph-tcp/start-tcp-server eval-handler {:port 1234}))

;(def client (aleph-tcp/tcp-client {:port 1234}))
;(def client (-> (Socket. "localhost" 1234) clojure.java.io/writer))


