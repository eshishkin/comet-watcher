# syntax=docker/dockerfile:experimental
FROM maven:3.6.3-openjdk-14 AS builder

WORKDIR project

ARG PROFILE=local

COPY pom.xml pom.xml
COPY src src
COPY etc etc

RUN --mount=type=cache,target=/root/.m2 mvn -Dquarkus.profile=${PROFILE} -Puber-jar clean package


FROM adoptopenjdk:14-jre-hotspot

COPY --from=builder /project/target/comet-watcher-runner.jar .
COPY --from=builder /project/etc/wrapper-docker.sh .

RUN ["chmod", "+x", "wrapper-docker.sh"]

CMD ["./wrapper-docker.sh"]

