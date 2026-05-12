# LOL-DNA

Note: This Web-App was build for education purposes and is not production ready. It was inspired by [u.gg](https://u.gg), [op.gg](https://op.gg) and [dpm.lol](https://dpm.lol) and is a minimal clone of these applications.
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
```text
LOL-DNA/
├── data/
│   └── ...                          # project data files
├── Front End/
│   ├── package.json                 # frontend-related npm config
│   └── vue-project/
│       ├── public/
│       │   └── favicon.ico
│       ├── src/
│       │   ├── assets/              # static frontend assets
│       │   ├── components/          # Vue components
│       │   ├── App.vue              # root Vue component
│       │   └── main.js              # frontend entry point
│       ├── index.html               # Vite HTML entry
│       ├── package.json             # Vue app dependencies
│       └── vite.config.js           # Vite configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/loldna/  # backend Java source code
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application.yml
│   │       └── templates/
│   │           └── ServiceImpl.java.ft
│   └── test/
│       └── java/
│           └── com/example/loldna/  # backend test source code
├── .gitignore
├── mvnw
├── mvnw.cmd
├── package.json
├── package-lock.json
├── pom.xml
└── README.md
```


## Configuration

The application expects a Riot API key through environment variables or a local config file.

### `application.yml`
The Riot API key is read like this:
```yaml
 riot:
   api:
     key: ${RIOT_API_KEY:}
```

### Option 1: Environment variable
Set an environment variable named:
```text
RIOT_API_KEY
```

### Option 2: Local config file
Create a local file:
```text
 src/main/resources/application-local.yml
 ```

Example:
```yaml
 riot:
   api:
     key: YOUR_RIOT_API_KEY
 ```

> `application-local.yml` is intended for local overrides and should not be committed.

## Database

The project uses an **H2 file database** stored at:
```text
./data/mydb
```

Configured in `application.properties`: 
```properties
 spring.datasource.url=jdbc:h2:file:./data/mydb spring.jpa.hibernate.ddl-auto=update
 ```


## Running the Backend

### With Maven Wrapper
On Windows:
```bash
 mvnw.cmd spring-boot:run
 ```


On macOS/Linux:
```bash
 ./mvnw spring-boot:run
 ```

### Or build first
```bash
 mvnw.cmd clean install
 ```

Then run the jar from `target/`.

## API Endpoints

### Get player overview
```http
 GET /api/players/{gameName}/{tagLine}
 ```

Example:

```http
 GET /api/players/Schneckify/Lost
 ```


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

This project is for educational / personal development purposes only.
