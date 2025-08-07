# Todo List Service

Backend service for managing a simple to-do list. Built with best practices, code quality, testing and clean API design.

## Tech Stack

- Java 17
- Spring Boot
- H2 Database (in-memory)
- Spring Data JPA
- JUnit + Spring Boot tests
- Maven
- Jacoco coverage
- Docker support
- Scheduler for background jobs


## Features

The service must expose a RESTful API to perform the following actions:

- Add an item: Create a new to-do item.
- Update an item's description: Change the text of an existing item.
- Mark an item as "done": Change an item status to "done".
- Mark an item as "not done": Change an item status back to "not done".
- Get an item: Retrieve the full details of a single to-do item.
- Get all "not done" items: Retrieve a list of all items with the status "not done". 

This endpoint include an option to fetch all items, regardless of their status.


## Project Highlights

### Domain-driven design structure with clear separation of concerns
  
- `model/` – Domain models (`ToDoItem`, `Status`)
- `repository/` – Data access using Spring Data JPA
- `service/` – Business logic (handling `OVERDUE` logic)
- `controller/` – RESTful API endpoints

### Enum-based Status Logic

- `Status` is a Java enum (`NOT_DONE`, `DONE`, `OVERDUE`) with strict validation

### Validation & Exception Handling
  
- Field validations using
- Custom error responses with structured error messages

### Automatic Status Updates
  
- Scheduled task marks items as `OVERDUE` when `dueDatetime` is in the past

### OpenAPI/Swagger Documentation

- Auto-generated swagger docs
- Includes examples, response descriptions, and parameter info

### Testing

- Unit and integration tests using JUnit and Spring Boot Test
- Code coverage via JaCoCo 

### 🐳 Docker Support

- Fully dockerized build and run


## Running the Project

### Run Project 

- Clone the project `git clone git@github.com:mostafa-kenawey/todo-list.git`
- Then `cd todo-list`
- Run `./mvnw spring-boot:run`

### Run Tests

- Run `./mvnw test` To run the tests

### H2 Console

- Open `http://localhost:8080/h2-console`
- JDBC URL `jdbc:h2:mem:todo_db`
- Username `sa`
- Password: (leave empty)


## Run with Makefile

A Makefile is provided to simplify development tasks

- Run `make help` To show the help message
- Run `make test` To run tests and start coverage server
- Run `make test-simple` To run tests using simple Docker approach
- Run `make coverage-server` To start only the coverage server
- Run `make clean` To clean up Docker containers and volumes

After running the make command:

- Open `http://localhost:8081/` To view the coverage report


## Run with Script

The easiest way to run tests and view coverage reports:

- Run `./run-tests.sh`

### This script will:

- Build a Docker image with Maven and Java 17
- Run all tests inside the container
- Generate JaCoCo coverage reports
- Start a web server to view the coverage report at http://localhost:8081


## Run with Docker

You can run using Docker

### Build and Run

- Run `docker build -t todo-app .` To build the Docker image
- Run `docker run -p 8080:8080 todo-app` To run the container

After running the docker:

- Open `http://localhost:8080/swagger-ui.html` For testing the API
- Open `http://localhost:8080/h2-console` To access the database

#### use Docker Compose:

- Run `docker-compose up --build`

### Open Test Coverage Report

- Run `docker-compose -f docker-compose.test.yml down --remove-orphans 2>/dev/null || true`
- Run `docker-compose -f docker-compose.test.yml up --build`
- Open `http://localhost:8081` In your browser to see the test coverage


## Scheduled Task

This application includes a scheduled background task using @EnableScheduling that automatically updates the status of todo items to OVERDUE when: 

- The current status is NOT_DONE 
- The dueDatetime is in the past 

### How It Works

- It runs every 60 seconds (configurable). 
- It logs its execution start and end. 
- It checks the database for overdue items. 
- It updates matching items’ statuses to OVERDUE.
- Once an item is marked as OVERDUE, it won’t be selected again. 
- You can adjust the frequency by changing the @Scheduled(fixedRate = ...) value.


## Code Quality and Static Analysis

The project uses the following tools to ensure high code quality and consistency:

- **JaCoCo** – Measures test coverage.
    - Run: `./mvnw test jacoco:report`
    - Clean Report `./mvnw clean test jacoco:report`
    - Report: `target/site/jacoco/index.html`

- **Checkstyle** – Validates code style using Google’s code conventions.
    - Run: `mvn checkstyle:check`

- **PMD** – Detects common coding issues and potential bugs.
    - Run: `mvn pmd:check`
    - Report: `target/site/pmd.html`

- **SpotBugs** – Performs static analysis to find bugs and vulnerabilities.
    - Run: `mvn spotbugs:check`
    - Report: `target/spotbugsXml.xml`


## API & Documentations

### Swagger UI

You can explore and test the API interactively using Swagger UI, Make sure the application is running locally before accessing the link.

- Open `http://localhost:8080/swagger-ui.html`

### Postman Collection

You can also use Postman to test the API.

- Open Postman and Import this file, All API requests will be available in the collection. `/postman/ToDoList.postman_collection.json`


## Future Improvements

- API Versioning
- Unified status update API (mark done or not done via one endpoint)
- Improve Error Aggregation, Support multiple field errors in a single response
- More Robust Overdue Logic and calculate overdue based on dueDatetime also, not status
- Caching for frequent read operations