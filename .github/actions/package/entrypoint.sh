#!/bin/bash

set -e

lein test :free

if [[ "$1" == "tests" ]]; then
    exit 0
fi

printf "%s" "$FUNDING_WALLET_CONFIG" > dev-rc/funding-wallet.json

if [[ "$1" == "release" ]]; then
    lein change version set "\"$2\""
fi

lein deploy clojars

