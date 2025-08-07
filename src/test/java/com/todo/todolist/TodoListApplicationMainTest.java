package com.todo.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

/**
 * Test class for the main method of TodoListApplication.
 */
class TodoListApplicationMainTest {

    @Test
    void testMain() {
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            String[] args = {"test"};
            
            TodoListApplication.main(args);
            
            mockedSpringApplication.verify(() -> 
                SpringApplication.run(TodoListApplication.class, args)
            );
        }
    }
}
