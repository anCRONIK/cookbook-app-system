# cookbook-backend

Backend application for cookbook app

not using relational database because NoSql databases are better for this purpose as we have only two entities and if we
would use tables, the one for ingredients would be huge (can be separated into few table depending on recipe type (
desert, main course, appetizer, etc))

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

To set up environment locally for acceptance tests, just run _docker-compose_ in `support` directory. After all
containers are up, use following table for property values:

| property           | value     |
|--------------------|-----------|
| __cassandra_host__ | localhost |
| __cassandra_port__ | 9042      |