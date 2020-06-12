# syntax=docker/dockerfile:experimental
FROM maven:3.6.3-openjdk-14 AS builder

WORKDIR project

ARG PROFILE=local

COPY pom.xml pom.xml
COPY src src
COPY etc etc

RUN --mount=type=cache,target=/root/.m2 mvn -Dquarkus.profile=${PROFILE} -Puber-jar clean package


FROM adoptopenjdk:14-jre-hotspot
ENV PORT=8080
COPY --from=builder /project/target/comet-watcher-runner.jar .
ENTRYPOINT ["java", "-Dquarkus.http.port=${PORT}","-jar", "comet-watcher-runner.jar"]

