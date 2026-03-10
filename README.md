# LOL-DNA

LOL-DNA is a full-stack League of Legends stats application built with **Spring Boot**, **Spring Data JPA**, **H2 Database**, and a separate frontend workspace.  
It fetches player and match-related data through the **Riot API** and exposes it through REST endpoints.

## Features

- Look up a player by **game name** and **tag line**
- Fetch and return a **player overview**
- Store match-related data in a local **H2 database**
- Expose backend data through REST APIs
- CORS enabled for frontend integration

## Tech Stack

### Backend
- **Java**
- **Spring Boot**
- **Spring Web / Spring MVC**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **Maven**

### Frontend
- Separate frontend folder available in the project (`Front End`)
- Project also includes Node/NPM setup

## Project Structure


## Configuration

The application expects a Riot API key through environment variables or a local config file.

### `application.yml`
The Riot API key is read like this:


### Option 1: Environment variable
Set an environment variable named:


### Option 2: Local config file
Create a local file:


Example:


> `application-local.yml` is intended for local overrides and should not be committed.

## Database

The project uses an **H2 file database** stored at:./data/mydb

Configured in `application.properties`: properties spring.datasource.url=jdbc:h2:file:./data/mydb spring.jpa.hibernate.ddl-auto=update


## Running the Backend

### With Maven Wrapper
On Windows:
bash mvnw.cmd spring-boot:run


On macOS/Linux:
bash ./mvnw spring-boot:run

### Or build first
bash mvnw.cmd clean install

Then run the jar from `target/`.

## API Endpoints

### Get player overview
http GET /api/players/{gameName}/{tagLine}

Example:

http GET /api/players/Schneckify/Lost


## Development Notes

- The backend allows requests from any origin via `@CrossOrigin(origins = "*")`
- Logging is enabled for Spring Web and the application package
- Match and player data are managed through service and repository layers

## Requirements

Make sure you have:

- **Java 21+**
- **Maven** or use the included **Maven Wrapper**
- **Node.js + npm** if you want to work on the frontend
- A valid **Riot API key**

## Possible Improvements

- Add authentication / rate-limit handling for Riot API requests
- Improve error responses for invalid player lookups
- Add frontend setup instructions
- Add tests for services and controllers
- Add Swagger / OpenAPI documentation

## License

This project is for educational / personal development use unless you define a license separately.