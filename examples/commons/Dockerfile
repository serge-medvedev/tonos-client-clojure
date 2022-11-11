FROM clojure:lein-bullseye

RUN apt-get update && apt-get install --no-install-recommends -y \
    gzip \
    wget

RUN wget http://sdkbinaries-ws.tonlabs.io/tonclient_1_38_1_linux.gz -O /usr/lib/libton_client.so.gz \
    && gunzip /usr/lib/libton_client.so.gz

WORKDIR /usr/src

RUN lein new app example

WORKDIR /usr/src/example

RUN lein change :dependencies conj '[tonos-client "1.38.1"]'

RUN lein deps

COPY core.clj src/example/core.clj

CMD ["lein", "run"]

