Shedlock test
---

This is a test project for [Shedlock](https://www.baeldung.com/shedlock-spring) library.

### How to run

1. Run `docker-compose up` to start the database
2. Run `mvn spring-boot:run` to start the application
3. Run `curl http://localhost:8080/lock` to test the lock

### How to test

1. Run `docker-compose up` to start the database
2. Run `mvn test` to run the tests
3. Run `curl http://localhost:8080/lock` to test the lock

### How to test with Docker

1. Run `docker-compose up` to start the database
2. Run `mvn clean package` to build the application
3. Run `docker build -t shedlock-test .` to build the Docker image
4. Run `docker run -p 8080:8080 shedlock-test` to run the Docker image
5. Run `curl http://localhost:8080/lock` to test the lock

### How to test with Docker Compose

1. Run `docker-compose up` to start the database
2. Run `mvn clean package` to build the application
3. Run `docker-compose build` to build the Docker image
4. Run `docker-compose up` to run the Docker image
5. Run `curl http://localhost:8080/lock` to test the lock

### How to test with Docker Compose and multiple instances

1. Run `docker-compose up` to start the database
2. Run `mvn clean package` to build the application
3. Run `docker-compose build` to build the Docker image
4. Run `docker-compose up --scale shedlock-test=3` to run the Docker image
5. Run `curl http://localhost:8080/lock` to test the lock

---
