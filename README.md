__In development

Complete app system for some simple online _Cookbook_.
This project is created for learning new technologies and sharpening current skill. It shouldn't be used in a finished product.

All projects are in single repository to be more accessible, otherwise, every app would have own repository.

Full app system will consist of:
- backend application (Spring Boot, Spring HATEOAS (HAL structure), Cassandra, JWT for security, integration with CDN for images/videos, Memcached) : handling all operations regarding to the application
- user auth application (Spring Boot, Spring OAUTH, Postgres) : handling user registration and authentication
- notification application (Spring Boot, Rabbit MQ) : sends all notifications
- frontend application (ReactJS)
- mobile application (Flutter)
- databases: Postgres, Cassandra
- CDN ?
- LB, RP: Nginx
- monitoring (ELK ?)
- CI/CD: for now Github actions
- hosting (GP?)
- TODO: try to find some use case for JMS or streaming integration__
