package com.todo.todolist;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Todo List application.
 */
@SpringBootApplication
@EnableScheduling
public class TodoListApplication {

  /**
   * Main method to start the Spring Boot application.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(TodoListApplication.class, args);
  }

  /**
   * Configure OpenAPI documentation.
   *
   * @return configured OpenAPI object
   */
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .info(new Info()
            .title("Todo API")
            .version("1.0")
            .description("API Documentation"))
        .addServersItem(new Server().url("/"));
  }

  /**
   * Configure GroupedOpenApi for todos group.
   *
   * @return configured GroupedOpenApi object
   */
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("todos")
        .pathsToMatch("/**")
        .build();
  }
}
