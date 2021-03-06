# Comet Watcher

![Java CI with Maven](https://github.com/eshishkin/comet-watcher/workflows/Java%20CI%20with%20Maven/badge.svg)
![Build with GraalVM](https://github.com/eshishkin/comet-watcher/workflows/Build%20with%20GraalVM/badge.svg)

Comet Watcher is an experimental application written using [Quarkus](https://quarkus.io) framework. The main purpose of the 
app is to pull data about upcoming comets from [Heavens-Above](https://heavens-above.com) and send it 
to users subscribed to such data. For now subscribers can be added via HTTP calls, however UI part is in 
development.


## How to build it locally

### Requirements
- Apache Maven 3.6.2 and above
- Java 11 and above
- GraalVM 19.3.1 and above (for native images)
- Docker (for running integration tests and building through docker)


### Building with Maven Wrapper

This repository contains maven wrapper that automatically downloads required version of Apache Maven.

The following command will build an artifact anr run a set of tests

```
./mvnw clean verify
```  

To compile the app without any checks `no-checks` profile can be used


```
./mvnw -Pno-checks clean verify
```

Native images are build using `native` maven profile


```
./mvnw -Pno-checks,native clean verify
```

### Building with docker

The main advantage of Docker is that you do not need to have Java or GraalVM installed. 
There are two dockerfiles in the project - for ordinary JVM and for GraalVM.
I use buildkit to build docker images and cache maven dependencies, so it requires different syntax a bit.

To build an image the following commands can be used

```
DOCKER_BUILDKIT=1 docker build --build-arg PROFILE={your profile} -t comet-watcher:latest .

DOCKER_BUILDKIT=1 docker build -f Dockerfile.native --build-arg PROFILE={your profile} -t comet-watcher:latest .
```

**NB:** Due to the nature of native image build procedure, it is memory and time consuming operation. I was very surprised
when it took 5-7 minutes to build the app. 

### Running the application locally

#### Using docker-compose

There is a convenient way to run the application using docker-compose file.
To run it locally you need 

- Build the app using `docker-test` quarkus profile 
    ```
    DOCKER_BUILDKIT=1 docker build --build-arg PROFILE=docker-test -t eshishkin/comet-watcher:latest .
    ```
- Run [docker-compose](etc/docker-compose-local/docker-compose.yml) via `docker-compose up` command

It will run the application on port `18080` and all required services (smtp, mongo, vault)

#### Using Minikube

To run the application in Kubernetes (Minikube) the following steps should be done
- Run `eval $(minikube docker-env)` so, all docker images will be created in Minikube itself rather than local docker 
- Build the app using `k8s` quarkus profile 
    ```
    DOCKER_BUILDKIT=1 docker build --build-arg PROFILE=k8s -t eshishkin/comet-watcher:latest .
    ```
- Go to `etc/k8s/comet-watcher` and execute `helm dependency update`
- Go to `etc/k8s` and execute `helm upgrade -i comet-watcher comet-watcher/`
- Run `minikube service comet-watcher --url`
- Open the url in any browser