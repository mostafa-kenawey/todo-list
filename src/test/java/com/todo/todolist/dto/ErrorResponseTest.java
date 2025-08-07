package com.todo.todolist.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    @DisplayName("Should correctly create and return values from ErrorResponse")
    void testErrorResponseValues() {
        String error = "Bad Request";
        String message = "Invalid input data";
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 400;

        ErrorResponse response = new ErrorResponse(error, message, timestamp, status);

        assertThat(response.getError()).isEqualTo(error);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getStatus()).isEqualTo(status);
    }
}
