#!/bin/bash

exec java -Denv.port=$PORT \
          -Denv.vault.url=$VAULT_HOST \
          -Denv.vault.token=$VAULT_TOKEN \
          -Xms64m -Xmx64m -Xss1m \
          -jar comet-watcher-runner.jar