FROM clojure:lein-bullseye

ADD https://raw.githubusercontent.com/tonlabs/EVER-SDK/1.45.0/tools/api.json /tmp/api.json
ADD http://sdkbinaries-ws.tonlabs.io/tonclient_1_45_0_linux.gz /usr/lib/libton_client.so.gz
RUN gunzip /usr/lib/libton_client.so.gz

WORKDIR /usr/src

COPY project.clj project.clj

RUN lein deps

COPY . .

RUN lein test :free

