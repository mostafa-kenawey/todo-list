package com.todo.todolist.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatusTest {

    @Test
    void testAllEnumValuesExist() {
        assertThat(Status.values()).containsExactly(Status.NOT_DONE, Status.DONE, Status.OVERDUE);
    }

    @Test
    void testValueOf() {
        assertThat(Status.valueOf("NOT_DONE")).isEqualTo(Status.NOT_DONE);
        assertThat(Status.valueOf("DONE")).isEqualTo(Status.DONE);
        assertThat(Status.valueOf("OVERDUE")).isEqualTo(Status.OVERDUE);
    }

    @Test
    void testInvalidValueOfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void testToString() {
        assertThat(Status.NOT_DONE.toString()).isEqualTo("NOT_DONE");
        assertThat(Status.DONE.toString()).isEqualTo("DONE");
    }
}
