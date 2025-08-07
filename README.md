# Todo List Service

Backend service for managing a simple to-do list. Built with best practices, code quality, testing and clean API design.

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