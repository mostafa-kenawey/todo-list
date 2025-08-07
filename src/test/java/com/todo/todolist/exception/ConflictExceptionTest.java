package com.todo.todolist.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConflictExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "already exists";
        ConflictException exception = new ConflictException(message);

        assertEquals(message, exception.getMessage());
    }
}
