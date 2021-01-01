FROM clojure:lein-buster

ADD https://raw.githubusercontent.com/tonlabs/TON-SDK/1.5.2/tools/api.json /tmp/api.json
ADD http://sdkbinaries-ws.tonlabs.io/tonclient_1_5_2_linux.gz /usr/lib/libton_client.so.gz
RUN gunzip /usr/lib/libton_client.so.gz

WORKDIR /usr/src

COPY . .

RUN lein test :free

