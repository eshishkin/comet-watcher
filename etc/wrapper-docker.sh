#!/bin/bash

java -Dquarkus.http.port=$PORT -Xms64m -Xmx64m -Xss1m -jar comet-watcher-runner.jar