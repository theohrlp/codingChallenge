# Implementation Document for Match and MatchOdd Management Application

## Table of Contents

1. [Project Overview](#project-overview)
2. [Controllers](#controllers)
    - [MatchController](#matchcontroller)
    - [MatchOddController](#matchoddcontroller)
3. [Services](#services)
    - [MatchService](#matchservice)
    - [MatchOddService](#matchoddservice)
4. [Database](#database)
    - [Schema Design](#schema-design)
    - [Initialization Scripts](#initialization-scripts)
    - [Configuration](#configuration)
5. [Dockerization](#dockerization)
    - [Docker Compose File](#docker-compose-file)
    - [Dockerfile for the Application](#dockerfile-for-the-application)
6. [Error Handling](#error-handling)

---

## Project Overview

This project manages `Match` and `MatchOdd` entities. It supports creating, updating, retrieving, and deleting matches and associated match odds. The backend is implemented in **Java (Spring Boot)** with a **PostgreSQL** database. The application is containerized using Docker.

---

## Controllers

### MatchController

- **Endpoints**:
    - `POST /matches`: Create a new match.
    - `GET /matches/{id}`: Retrieve match details by ID.
    - `PUT /matches/{id}`: Update a match by ID.
    - `DELETE /matches/{id}`: Delete a match by ID.

- **Highlights**:
    - Input validation using `@Valid` and custom `@UUIDConstraint`.
    - Exceptions like `ResourceNotFoundException` for missing matches and `MismatchedIdsException` for ID mismatches.

### MatchOddController

- **Endpoints**:
    - `POST /matches/{matchId}/odds`: Add match odds to a match.
    - `GET /matches/{matchId}/odds`: Retrieve odds for a specific match.
    - `DELETE /matches/{matchId}/odds/{oddId}`: Delete specific match odds.

- **Highlights**:
    - Ensures that `matchId` in the request path matches the ID in the request body.
    - Throws custom exceptions like `MismatchedMatchIdException` for mismatched IDs.

---

## Services

### MatchService

- **Core Methods**:
    - `saveMatch(MatchModel match)`: Saves a match to the database.
    - `getMatch(UUID id)`: Retrieves a match by ID.
    - `updateMatch(MatchModel match)`: Updates a match.
    - `deleteMatch(UUID id)`: Deletes a match by ID.

- **Implementation Details**:
    - Utilizes `MatchRepository` to interact with the database.
    - Maps between `MatchEntity` and `MatchModel` using `ModelMapper`.

### MatchOddService

- **Core Methods**:
    - `addOddsToMatch(UUID matchId, MatchOddModel odds)`: Adds odds to a specific match.
    - `getOddsByMatchId(UUID matchId)`: Retrieves odds for a match.
    - `deleteOdds(UUID matchId, UUID oddId)`: Deletes specific odds.

- **Implementation Details**:
    - Ensures referential integrity between matches and odds.
    - Uses `MatchOddRepository` for database operations.

---

## Database

### Schema Design

- **Tables**:
    - `matches.match`: Stores match details.
        - Columns: `id (UUID)`, `description`, `team_a`, `team_b`, `sport`
    - `matches.match_odds`: Stores match odds.
        - Columns: `id (UUID)`, `match_id (FK)`, `odd`, `specifier`.

### Initialization Scripts

Located in `./init-scripts/init.sql`:
```sql
CREATE SCHEMA matches;

CREATE TABLE matches.match (
    id uuid,
    description VARCHAR(255) NOT NULL,
    match_date DATE NOT NULL,
    match_time TIME NOT NULL,
    team_a VARCHAR(100) NOT NULL,
    team_b VARCHAR(100) NOT NULL,
    sport VARCHAR(50) NOT NULL
);

CREATE TABLE matches.match_odds (
    id uuid,
    match_id uuid NOT NULL,
    specifier VARCHAR(50) NOT NULL,
    odd NUMERIC(5, 2) NOT NULL CHECK (odd > 0),
    FOREIGN KEY (match_id) REFERENCES matches.match (id) ON DELETE CASCADE
);


```

## 5. Dockerization

This section explains how to containerize the application using Docker.

### Docker Compose File

The `docker-compose.yml` file orchestrates the PostgreSQL database and the application.

```yaml
version: '3.8'

services:
  postgres:
    image: 'postgres:latest'
    container_name: 'postgres_db'
    environment:
      - 'POSTGRES_DB=challengeDB'
      - 'POSTGRES_PASSWORD=root'
      - 'POSTGRES_USER=root'
    ports:
      - '5432:5432'
    volumes:
      - './init-scripts:/docker-entrypoint-initdb.d'
    restart: 'always'
    healthcheck:
      test: [ "CMD", "pg_isready", "-U", "root", "-d", "challengeDB" ]
      interval: '10s'
      retries: 10
      timeout: '20s'
      start_period: '5s'

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: 'java_app'
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/challengeDB
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    ports:
      - "8080:8080"

volumes:
  pgdata:
```

## 6. Error Handling

This section covers the custom exceptions and global exception handler for managing errors in the application.

### Custom Exceptions

#### `ResourceNotFoundException`
Thrown when an entity is not found in the database.

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```
