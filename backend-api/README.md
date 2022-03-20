# cookbook-backend

Backend application for cookbook app

# Intro

Application provides basic endpoints which are going to be used by frontend applications. Current API version is _1_. In
the future, in case of some API changes, that API should be accessible with new version number, while the older versions
are still going to be supported.

# Response format

All endpoint responses are in JSON format. Response format follows HAL principle, so you can expect the content type
as `application/hal+json`. In case of some error, response is going to have content type `application/hal+json`. But do
not rely on this, future implementation can be changed.

# Dependencies

Application uses Apache Cassandra and the authentication application as major dependencies.

## Apache Cassandra

It is used as main database for writing/reading data. If database is not available, application will stop running.

## Authentication application

It is used for two purposes:

- JWT validation
- it creates authors using `AuthorController` endpoint when the user is successfully registered in our system

## Memcache

TODO

## CDN

It is used for uploading images that are used for thumbnails, covers and profiles.

TODO

# API documentation

TODO: swagger doc

# Security

jwt ...

# Rate limits

TODO... define them etc

# Local setup

There is `docker-compose.yml` file in the project root directory. That file depends on the `liquibase` project which is
expected to be in the same hierarchy as this project (this means that _backend-api_ and _liquibase_ project should be in
the same directory). Docker compose will start Apache Cassandra the Wiremock for all other dependencies. After Cassandra
is "alive", keypoint will be created and liquibase will create needed tables and initial data.

# Tests

By default, only unit test are running in maven lifecycle. Integration and acceptance tests are separated from that
lifecycle and are called using maven profiles.

All tests must have the __tag__ which is used as group by surefire and failsafe maven plugins.

Every class must have at least 90% coverage, otherwise build will fail. If you need to exclude some class from coverage,
add it to the jaccoco exclusion list.
(Jaccoco is only checking coverage for unit tests).

## Unit tests

- in default maven `test` directory
- tagged with __UnitTest__
- follow maven naming convention (in this case, all test end with suffix _Test_)

## Integration tests

- in separate directory called `integration-test`
- they are using Docker, so they can be only run on machine with docker support
- follow maven naming convention (in this case, all test end with suffix _IT_)
- use test containers for any dependency
- run with `mvn clean verify -P integration-test`
- you can run test in IDEA the same way as unit tests

## Acceptance Tests

- in separate directory called `integration-test` under package `acceptance`
- Cucumber as testing framework
- run with `mvn clean verify -P acceptance-test -Dcassandra_host=...`
- to run them from IDEA you need to use `AcceptanceTestRunner` class and provide needed VM variables.

To run acceptance tests, you need to provide following system properties:

| property           | description                                                           |
|--------------------|-----------------------------------------------------------------------|
| __cassandra_host__ | ip or hostname of the cassandra instance that can be used for testing |
| __cassandra_port__ | port on which Cassandra instance is listening                         |

### Local setup

To set up environment locally for acceptance tests, read chapter about _Local setup_  and after that setup,
run ` docker-compose up -d` in project root. After all containers are up, use following table for property values:

| property           | value     |
|--------------------|-----------|
| __cassandra_host__ | localhost |
| __cassandra_port__ | 9042      |