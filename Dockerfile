# syntax=docker/dockerfile:experimental
FROM maven:3.6.3-openjdk-14 AS builder

WORKDIR project

ARG PROFILE=local

COPY pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2 mvn -Puber-jar -f pom.xml -B de.qaware.maven:go-offline-maven-plugin:1.2.5:resolve-dependencies

COPY src src
COPY etc/checkstyle etc/checkstyle
COPY etc/spotbugs etc/spotbugs

RUN --mount=type=cache,target=/root/.m2 mvn -Dquarkus.profile=${PROFILE} -Puber-jar clean package

COPY etc/wrapper-docker.sh etc/wrapper-docker.sh


FROM adoptopenjdk:14-jre-hotspot

COPY --from=builder /project/target/comet-watcher-runner.jar .
COPY --from=builder /project/etc/wrapper-docker.sh .

RUN ["chmod", "+x", "wrapper-docker.sh"]

CMD ["./wrapper-docker.sh"]

