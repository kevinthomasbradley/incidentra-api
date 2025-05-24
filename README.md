# Incidentra
Incidentra is a demo project that applies system design principles using various technologies (Spring Boot, Postgres, Kaftka, Elasticsearch, Nginix, ReactJS, React Native). It showcases practical application development and explores architectural concepts in a real-world context.

## Prerequisites

- [Docker](https://www.docker.com/get-started)
- [Java 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)

## Getting Started

### 1. Start PostgreSQL with Docker

```sh
docker run --name postgresdb -e POSTGRES_PASSWORD=admin -d -p 5432:5432 postgres
```

### 2. Load Database Schema

Copy the schema file into the container and apply it:

```sh
docker cp ./src/main/resources/postgres/schema.sql postgresdb:/schema.sql
docker exec -it postgresdb bash
psql -U postgres --file schema.sql
```

### 3. Load Sample Data

Copy the data file and apply it:

```sh
docker cp ./src/main/resources/postgres/data.sql postgresdb:/data.sql
docker exec -it postgresdb bash
psql -U postgres --file data.sql
```

### 4. Verify Data

Connect to the database and check the data:

```sh
docker exec -it postgresdb psql -U postgres -d incident_db -c "SELECT * FROM incidents;"

docker restart postgresdb
```

## Running the Application

Build and run the Spring Boot application:

```sh
./mvnw spring-boot:run
```

The API will be available at [http://localhost:8080](http://localhost:8080).

## Example API Usage

### Create a User

```sh
curl -X POST -H "Content-Type: application/json" -d '{
  "username": "john",
  "password": "pass123",
  "email": "john@example.com",
  "role": "CITIZEN"
}' http://localhost:8080/api/users
```

### Create an Incident

```sh
curl -X POST -H "Content-Type: application/json" -d '{
  "description": "Pothole on Main St",
  "citizenId": 1
}' http://localhost:8080/api/incidents
```

### Assign an Incident

```sh
curl -X PUT -H "Content-Type: application/json" -d '{
  "dispatcherId": 3,
  "responderId": 4
}' http://localhost:8080/api/incidents/1/assign
```

---

For more details, see the source files in [`src/main/java/com/kevinthomasbradley/incidentapi`](src/main/java/com/kevinthomasbradley/incidentapi).


TODO
- fix user tests
- add incident tests

./mvnw test