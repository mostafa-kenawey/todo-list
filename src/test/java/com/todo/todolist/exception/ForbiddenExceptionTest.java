package com.todo.todolist.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "forbidden action";
        ForbiddenException exception = new ForbiddenException(message);

        assertEquals(message, exception.getMessage());
    }
}
