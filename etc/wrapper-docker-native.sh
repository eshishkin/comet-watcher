#!/bin/bash

exec ./comet-watcher-runner -Denv.port=$PORT \
          -Denv.vault.url=$VAULT_HOST \
          -Denv.vault.token=$VAULT_TOKEN \
          -Djavax.net.ssl.trustStore=cacerts \
          -Xms64m -Xmx64m -Xss1m