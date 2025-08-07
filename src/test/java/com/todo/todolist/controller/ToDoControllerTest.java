package com.todo.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.service.ToDoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToDoController.class)
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoService toDoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ToDoItem item;

    @BeforeEach
    void setUp() {
        item = new ToDoItem();
        item.setId(1L);
        item.setDescription("Test Task");
        item.setDueDatetime(LocalDateTime.of(2025, 8, 10, 12, 0));
        item.setStatus(Status.NOT_DONE);
    }

    @Test
    void testGetAllItems() throws Exception {
        Mockito.when(toDoService.getAllItems(Optional.empty())).thenReturn(Arrays.asList(item));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test Task"));
    }

    @Test
    void testGetItemById() throws Exception {
        Mockito.when(toDoService.getItemById(1L)).thenReturn(item);

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Task"));
    }

    @Test
    void testCreateItem() throws Exception {
        ToDoItem itemToCreate = new ToDoItem();
        itemToCreate.setDescription("New Task");
        itemToCreate.setDueDatetime(LocalDateTime.of(2025, 8, 10, 12, 0));

        ToDoItem created = new ToDoItem();
        created.setId(2L);
        created.setDescription("New Task");
        created.setDueDatetime(itemToCreate.getDueDatetime());
        created.setStatus(Status.NOT_DONE);

        Mockito.when(toDoService.createItem(any(ToDoItem.class))).thenReturn(created);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.status").value("NOT_DONE"));
    }

    @Test
    void testUpdateItem() throws Exception {
        ToDoItem updated = new ToDoItem();
        updated.setId(1L);
        updated.setDescription("Updated Task");
        updated.setDueDatetime(item.getDueDatetime());
        updated.setStatus(Status.DONE);

        Mockito.when(toDoService.updateItem(eq(1L), any(ToDoItem.class))).thenReturn(updated);

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Task"))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void testMarkAsDoneEndpoint() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setId(1L);
        item.setStatus(Status.DONE);
        item.setDescription("Test");
        item.setDueDatetime(LocalDateTime.now().plusDays(1));
        item.setDoneDatetime(LocalDateTime.now());

        when(toDoService.markAsDone(1L)).thenReturn(item);

        mockMvc.perform(patch("/api/todos/1/done"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void testMarkAsNotDoneEndpoint() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setId(1L);
        item.setStatus(Status.NOT_DONE);
        item.setDescription("Test");
        item.setDueDatetime(LocalDateTime.now().plusDays(1));
        item.setDoneDatetime(LocalDateTime.now());

        when(toDoService.markAsNotDone(1L)).thenReturn(item);

        mockMvc.perform(patch("/api/todos/1/not-done"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("NOT_DONE"));
    }

    @Test
    void testDeleteItem() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
}
