package com.todo.todolist.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExceptionTriggerController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testBadRequestException() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Invalid request"))
            .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void testConflictException() throws Exception {
        mockMvc.perform(get("/test/conflict"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("Already Exists"))
            .andExpect(jsonPath("$.message").value("Conflict occurred"))
            .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void testForbiddenException() throws Exception {
        mockMvc.perform(get("/test/forbidden"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error").value("Forbidden"))
            .andExpect(jsonPath("$.message").value("Access denied"))
            .andExpect(jsonPath("$.status").value(403));
    }

     @Test
    void testNotFoundException() throws Exception {
        mockMvc.perform(get("/test/not-found"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Not Found"))
            .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testGeneralException() throws Exception {
        mockMvc.perform(get("/test/general-error"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error").value("Internal Server Error"))
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("An unexpected error occurred")))
            .andExpect(jsonPath("$.status").value(500));
    }
}
