[![Clojars Project](https://img.shields.io/clojars/vpre/tonos-client.svg)](https://clojars.org/tonos-client)
![Testing](https://github.com/serge-medvedev/tonos-client-clojure/workflows/tests/badge.svg)

# Clojure bindings to EVER SDK's Core Client Library

## Why Clojure?

Clojure is a modern and powerful functional programming language. A few great products use it for configuration and a whole lot more are written in it. The former case was the primary motivation to create this bindings library: to provide a native way to interact with the Everscale blockchain from within configuration pipelines of such event processing systems as [Riemann](https://riemann.io/) (written and configurable via Clojure) and [Apache Storm](https://storm.apache.org/) (supports Clojure-based DSL for configuration).

Having that in place, one can, for example, alter the behavior of his/her event processing pipelines depending on the blockchain state or change that state based on event patterns analysis. Since both Riemann and Apache Storm fit for arbitrary computations, the number of possible use cases is huge.

## Usage
### Prerequisites
- _libton_client.so_ is accessible somewhere at well-known locations
  ```console
  # wget http://sdkbinaries-ws.tonlabs.io/tonclient_1_44_3_linux.gz -O /usr/lib/libton_client.so.gz \
    && gunzip /usr/lib/libton_client.so.gz
  ```
### Example
```clojure
[tonos-client "1.44.3"]

; In the ns statement:
(ns my.ns
  (:require [tonos.client.core :as core]
            [tonos.client.client :as client]))

; In the code:
(let [config "{\"network\":{\"endpoints\":[\"eri01.net.everos.dev\"]}}"
      context (core/create-context config)]
  (println (client/version! context))
  (core/destroy-context context))
```

## Features

At its heart this bindings library utilizes JNA and Clojure's async channels to harness the power of the EVER OS SDK's Core Client Library.
The whole asynchronisity is abstracted away from the user via such a powerful construct of Clojure as __lazy sequences__, which leads to uniformity and consistency of user experience, being the main focus since the bindings code itself is completely auto-generated.

Let's see how it looks like.

There's what we do to subscribe to events, related to a particular _collection_, process them somehow and unsubscribe when ready:
```clojure
(let [events (net/subscribe-collection context params)
      handle (-> events first :params-json :handle)]
  (doseq [e (rest events)]
    (println e)
    (when (it-is-time-to-unsubscribe)
      (net/unsubscribe context {:handle handle}))))
```

Let's suppose that we're interested in the last event only:
```clojure
(-> (processing/process-message context params)
    doall
    last
    :params-json
    println)
```

Kinda verbose, isn't it? In fact, the pattern above is so common that there's a shortcut version for almost every function in the library. Note the exclamation mark at the end of a function name:
```clojure
(-> (processing/process-message! context params)
    println)
```
In such case, the function blocks until the underlying call is finished. Both versions might have their uses (see `process-message-test` and how `process-message` is being called in `fund-account`, for example).

One might point out that usage of lazy sequences comes with a bit of boilerplate code but when you realize the full potential of the approach, you gladly ignore that little drawback. __Higher order functions__ applicability worths a lot by itself. Just think: when blockchain events are represented by a lazy sequence, they become a subject for mapping, reducing, filtering and other useful functional techniques.

For example, let's retrieve a _shard block id_ while sending a message, using filtering:
```clojure
(let [shard-block-id (->> (processing/send-message context params)
                          (filter #(and (-> % :params-json (contains? :shard_block_id))
                                        (-> % :response-type (= 0))))
                          first
                          :params-json
                          :shard_block_id)]
  (println shard-block-id))
```

__Although this library is useful by itself, various wrappers and DSLs are expected to be built on top of it.__

## Building

The simplest way to build the library and run the tests is by having Docker installed.

When ready, build the image:
```console
$ docker build -t tonos-client-clojure .
```

## Testing

The library has over 50 tests.

There are four categories of them:
- fast & slow
- free & paid

And it's also good to know that:
- all __fast__ tests are __free__ and all __paid__ tests are __slow__
- all __free__ tests are being run automatically as a Docker image build step
- some tests depend on either DevNet or MainNet accessibility

The __paid__ tests are those which require account funding, e.g. for successful contract deployment. To run them, you need to:
- have a wallet on the [DevNet](https://net.ton.dev) with some tokens in it
- create a file called _funding-wallet.json_ under the _dev-rc_ directory based on [funding-wallet.json.example](dev-rc/funding-wallet.json.example)

When ready, do the following:
```shell
$ docker run --rm tonos-client-clojure lein test :paid
```
> NOTE: replace "paid" with another name to run specific category of tests. Get rid of any test selectors to run the whole test suite.
