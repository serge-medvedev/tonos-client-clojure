FROM clojure:lein-buster

RUN apt-get update && apt-get install --no-install-recommends -y \
    gzip \
    wget

RUN wget http://sdkbinaries-ws.tonlabs.io/tonclient_1_0_0_linux.gz -O tonclient.gz \
    && gunzip tonclient.gz \
    && mv tonclient /usr/lib/libton_client.so \
    && wget https://raw.githubusercontent.com/tonlabs/TON-SDK/1.0.0/tools/api.json -O /tmp/api.json

WORKDIR /usr/src

COPY . .

RUN lein test :free

