package com.todo.todolist.service;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.repository.ToDoItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ToDoStatusSchedulerTest {

    private ToDoItemRepository toDoItemRepository;
    private ToDoStatusScheduler toDoStatusScheduler;

    @BeforeEach
    void setUp() {
        toDoItemRepository = mock(ToDoItemRepository.class);
        toDoStatusScheduler = new ToDoStatusScheduler(toDoItemRepository);
    }

    @Test
    void testMarkOverdueItems_updatesStatusAndSaves() {
        ToDoItem item = new ToDoItem();
        item.setDescription("Old Task");
        item.setDueDatetime(LocalDateTime.now().minusDays(2));
        item.setStatus(Status.NOT_DONE);
        item.setId(1L);

        when(toDoItemRepository.findByStatusAndDueDatetimeBefore(eq(Status.NOT_DONE), any()))
                .thenReturn(List.of(item));

        toDoStatusScheduler.markOverdueItems();

        ArgumentCaptor<List<ToDoItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(toDoItemRepository).saveAll(captor.capture());

        List<ToDoItem> savedItems = captor.getValue();
        assertThat(savedItems).hasSize(1);
        assertThat(savedItems.get(0).getStatus()).isEqualTo(Status.OVERDUE);
    }

    @Test
    void testMarkOverdueItems_noItems_nothingSaved() {
        when(toDoItemRepository.findByStatusAndDueDatetimeBefore(eq(Status.NOT_DONE), any()))
                .thenReturn(Collections.emptyList());

        toDoStatusScheduler.markOverdueItems();

        verify(toDoItemRepository, never()).saveAll(any());
    }
}
