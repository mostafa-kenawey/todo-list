# Todo List Service

Backend service for managing a simple to-do list. Built with best practices, code quality, testing and clean API design.

## Features

The service must expose a RESTful API to perform the following actions:

- Add an item: Create a new to-do item.
- Update an item's description: Change the text of an existing item.
- Mark an item as "done": Change an item status to "done".
- Mark an item as "not done": Change an item status back to "not done".
- Get an item: Retrieve the full details of a single to-do item.
- Get all "not done" items: Retrieve a list of all items with the status "not done". 

This endpoint include an option to fetch all items, regardless of their status.

## Tech Stack

- Java 17
- Spring Boot
- H2 Database (in-memory)
- Spring Data JPA
- JUnit 5 (for testing)
- Maven

## Running the Project

- Clone the project `git clone git@github.com:mostafa-kenawey/todo-list.git`
- Run `./mvnw spring-boot:run`
- Open `http://localhost:8080/h2-console` For the H2 Console

## Run Tests

- Run `./mvnw test` To run the tests

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