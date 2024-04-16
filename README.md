# Kien Trung Tran - Wego Interview

## Assignment

For more details, please read it at [pdf](./Senior%20Software%20Engineer%20Coding%20Exercise.pdf) file.

## Documentation

Please read the [documentation](./docs/ANALYSIS.md) to have a better understanding of the system design and the approaches.

## Introduction

By default, the services are exposed on the following ports:

| Service          | Port      | Note        |
|------------------|-----------|-------------|
| Car Park Server  | ``:8080`` |             |
| Postgres/Postgis | ``:5432`` | Docker only |


## Prerequisites

Proper environment setup is crucial for the successful build and run of this project. Please make sure you have installed
necessary tools before building the project.

To wrap up, the following tools are:

| Tool     | Version  | Note                                                                  |
|----------|----------|-----------------------------------------------------------------------|
| [Docker] | '>=20.x' |                                                                       |
| [Gradle] | '>=8.x'  | Optional, Gradle Wrapper <sup>1</sup> can be used instead.            |
| [JDK]    | '21'     | Optional, Gradle Tool Chains <sup>2</sup> plugin can be used instead. |


<sup>1</sup>) [Gradle Wrapper] allows you to skip the Gradle installation. 

<sup>2</sup>) [Gradle Tool Chains] allows you to skip the JDK installation.

## Quickstart
Assuming you went through the documentation, your environment is set up and project is prepared, this is the most straightforward way to get your project application up and running:

**Compose the whole project in Docker**

`./gradlew composeUp`

After the whole project is up, the application is available at `http://localhost:8080`

Try the command to see the nearest car parks:

```
curl http://localhost:8080/carparks/nearest?latitude=1.37326&longitude=103.897&page=1&per_page=3
```
Or just simply copy the url to browser

```http request
http://localhost:8080/carparks/nearest?latitude=1.37326&longitude=103.897&page=1&per_page=3
```

## Gradle Tasks

To run build the project:

`./gradlew build`

To build the project's image:

`./gradlew buildImage`

To run tests:

`./gradlew check`

To compose a single postgis container:

`./gradlew postgresComposeUp`

>**NOTE**: If you wish to run the project with Intellj, make sure that the postgis container is up first.
 
## Framework & Library

Web Framework: [Spring Boot Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)

ORM: [Hibernate](https://hibernate.org/)

Migration: [Liquibase](https://www.liquibase.com/community)

Caching: [Hazelcast](https://hazelcast.com/)

Http Client: [Open Feign](https://github.com/OpenFeign/feign)

Testing: [Junit Jupiter](https://junit.org/junit5/docs/current/user-guide/)

Test Containers: [Test Container](https://testcontainers.com/)

Mock Server: [Wire Mock](https://wiremock.org/)

<!--- References --->
[Docker]: https://hub.docker.com/
[JDK]: https://adoptopenjdk.net/
[Gradle]: https://docs.gradle.org/
[Gradle Wrapper]: https://docs.gradle.org/current/userguide/gradle_wrapper.html
[Gradle Tool Chains]: https://docs.gradle.org/current/userguide/toolchains.html

