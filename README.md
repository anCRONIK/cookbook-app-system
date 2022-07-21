__In development

Complete app system for some simple online _Cookbook_.
This project is created to learn new technologies and sharpening current skills.
It shouldn't be used in a finished product.

All projects are in a single repository to be more accessible, otherwise, every app would have own repository.

Full-app system will consist of:
- backend application (Spring Boot, Spring HATEOAS (HAL structure), Cassandra, JWT for security, integration with CDN for images/videos, Memcached, AXON) : handling all operations regarding the application
- user auth application (Spring Boot, Spring OAUTH, Postgres, AXON) : handling user registration and authentication
- notification application (Spring Boot, Rabbit MQ) : sends all notifications
- internal tool for admins (Spring Boot, Postgres, React for Frontend)
- frontend application (ReactJS, TypeScript, ThailWind CSS)
- databases: Postgres, Cassandra
- CDN ?
- LB, RP: Nginx
- monitoring (ELK ?)
- CI/CD: for now GitHub actions
- hosting (GP?)
