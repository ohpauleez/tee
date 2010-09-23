(ns tee.jobs
  ^{:author "Paul deGrandis"
    :description "A clojure port of Zach Beane's macro that uses promises (http://xach.livejournal.com/268369.html)"})


;; TODO look into rolling in functionality or support for work (http://github.com/clj-sys/work)
;; TODO all of these should return the future they create (move to let)

(defmacro queue-job
  "Push a call/job off into a future, and conj the result to res-queue
  Arguments:
    res-queue - a ref'd data structure to conj results into (anything that support IPersistentStack)
    body - any form to eval (done in a future)
  Ex: (dosync (tee/queue-job tee/results (str 'Hello' 'World')))"
  ;[^clojure.lang.IPersistentStack res-queue & body]
  [res-queue & body]
  `(alter ~res-queue conj (future (do ~@body))))

(defn queue-job-call
  "Push a call/job off into a future, and conj the result to res-queue
  Arguments:
    res-queue - a ref'd data structure to conj results into (anything that support IPersistentStack)
    f - a function (anything the implements Callable)
  Ex:
    (dosync (tee/queue-job tee/results #(str 'Hello' 'World')))"
  ;[^clojure.lang.IPersistentStack res-queue ^Callable f]
  [res-queue ^Callable f]
  (alter res-queue conj (future (f))))

(defmacro queue-lazy-job
  "Wrap a call/job as a lazy-job, and conj the 'result' to res-queue
  Arguments:
    res-queue - a ref'd data structure to conj results into (anything that support IPersistentStack)
    body - any form to eval (done upon deref)
  Ex: (dosync (tee/queue-job tee/results (str 'Hello' 'World')))"
  [res-queue & body]
  `(alter ~res-queue conj (lazy-job (do ~@body))))

(defn lazy-job?
  "Predicate to test to see if something is a lazy job"
  [lj]
  (contains? (meta lj) :lazyjob))

(defn lazy-job-call
  "Returns a function that is called when it is deref'd
  Like a delay that doesn't cache
  Arguments:
    f - a function (or anything Callable)
  Ex: (def ljc (lazy-job-call #(str 'hello' ' world')))
      @ljc"
  [^Callable f]
  (if (lazy-job? f)
    f
    ^:lazyjob
    (reify clojure.lang.IDeref
      (deref [_] (f)))))

(defmacro lazy-job
  "Returns a function that is called when it is deref'd
  Like a delay that doesn't cache
  Ex: (def ljc (lazy-job (str 'hello' ' world')))
      @ljc"
  [& body]
  `(lazy-job-call (^{:once true} fn*  [] ~@body)))

