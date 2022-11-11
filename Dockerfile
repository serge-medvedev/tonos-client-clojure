FROM clojure:lein-bullseye

ADD https://raw.githubusercontent.com/tonlabs/TON-SDK/1.38.1/tools/api.json /tmp/api.json
ADD http://sdkbinaries-ws.tonlabs.io/tonclient_1_38_1_linux.gz /usr/lib/libton_client.so.gz
RUN gunzip /usr/lib/libton_client.so.gz

WORKDIR /usr/src

COPY project.clj project.clj

RUN lein deps

COPY . .

RUN lein test :free

