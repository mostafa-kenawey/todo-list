FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/todo-list-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-DwebAllowOthers=true", "-jar", "app.jar"]
