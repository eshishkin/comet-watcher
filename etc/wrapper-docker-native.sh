#!/bin/bash

exec ./comet-watcher-runner -Dquarkus.http.port=$PORT \
          -Denv.mongo.url=$MONGO_HOST \
          -Denv.mongo.port=$MONGO_PORT \
          -Denv.mongo.db=$MONGO_COMET_WATCHER_DB \
          -Denv.mongo.usr=$MONGO_COMET_WATCHER_USR \
          -Denv.mongo.pwd=$MONGO_COMET_WATCHER_PWD \
          -Denv.mail.smtp.user=$SENDGRID_USERNAME \
          -Denv.mail.smtp.password=$SENDGRID_PASSWORD \
          -Djavax.net.ssl.trustStore=cacerts \
          -Xms64m -Xmx64m -Xss1m