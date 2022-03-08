# cookbook-backend

Backend application for cookbook app

not using relational database because NoSql databases are better for this purpose as we have only two entities and if we
would use tables, the one for ingredients would be huge (can be separated into few table depending on recipe type (
desert, main course, appetizer, etc))

Liquibase should be run using mvn, need to add some migration tool that supports Cassandra to start with app.

Jacoco to run unit and integration test separately