FROM clojure:lein-bullseye

ADD https://raw.githubusercontent.com/tonlabs/TON-SDK/1.45.0/tools/api.json /tmp/api.json
ADD http://sdkbinaries-ws.tonlabs.io/tonclient_1_45_0_linux.gz /usr/lib/libton_client.so.gz
RUN gunzip /usr/lib/libton_client.so.gz

COPY entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

