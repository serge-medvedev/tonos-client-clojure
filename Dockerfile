FROM rust:1.46.0-buster as sdk

RUN apt-get update && apt-get install -y --no-install-recommends \
        git \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /usr/src

ENV TON_SDK_BRANCH=1.0.0-rc

RUN git clone -b $TON_SDK_BRANCH https://github.com/tonlabs/TON-SDK.git

RUN cd TON-SDK \
    && cargo update \
    && cargo build --release --manifest-path ton_client/client/Cargo.toml

FROM clojure:lein-buster

COPY --from=sdk /usr/src/TON-SDK/target/release/libton_client.so /usr/lib/

ADD https://raw.githubusercontent.com/tonlabs/TON-SDK/1.0.0-rc/tools/api.json /tmp/api.json

