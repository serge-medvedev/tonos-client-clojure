#!/bin/bash

set -e

if [[ "$1" == "tests" ]]; then
    lein test :free

    exit 0
fi

printf "%s" "$FUNDING_WALLET_CONFIG" > dev-rc/funding-wallet.json

lein test

if [[ "$1" == "release" ]]; then
    lein change version set "\"$2\""
fi

lein deploy clojars

