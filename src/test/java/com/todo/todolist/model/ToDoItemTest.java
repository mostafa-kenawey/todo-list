package com.todo.todolist.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ToDoItemTest {

    @Test
    void testDefaultStatusAndCreationDate() {
        ToDoItem item = new ToDoItem();

        assertThat(item.getStatus()).isEqualTo(Status.NOT_DONE);
        assertThat(item.getCreationDatetime()).isNotNull();
    }

    @Test
    void testSetAndGetAllFields() {
        ToDoItem item = new ToDoItem();

        Long id = 1L;
        String description = "Complete the unit test";
        Status status = Status.DONE;
        LocalDateTime creationTime = LocalDateTime.of(2025, 8, 5, 10, 0);
        LocalDateTime dueTime = LocalDateTime.of(2025, 8, 10, 12, 0);
        LocalDateTime doneTime = LocalDateTime.of(2025, 8, 6, 14, 0);

        item.setId(id);
        item.setDescription(description);
        item.setStatus(status);
        item.setCreationDatetime(creationTime);
        item.setDueDatetime(dueTime);
        item.setDoneDatetime(doneTime);

        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.getDescription()).isEqualTo(description);
        assertThat(item.getStatus()).isEqualTo(status);
        assertThat(item.getCreationDatetime()).isEqualTo(creationTime);
        assertThat(item.getDueDatetime()).isEqualTo(dueTime);
        assertThat(item.getDoneDatetime()).isEqualTo(doneTime);
    }

    @Test
    void testNullableDoneDatetime() {
        ToDoItem item = new ToDoItem();
        item.setDoneDatetime(null);

        assertThat(item.getDoneDatetime()).isNull();
    }
}
