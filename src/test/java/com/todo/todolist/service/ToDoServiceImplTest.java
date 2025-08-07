package com.todo.todolist.service;

import com.todo.todolist.exception.ResourceNotFoundException;
import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.repository.ToDoItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToDoServiceImplTest {

    private ToDoItemRepository repository;
    private ToDoServiceImpl service;

    @BeforeEach
    void setup() {
        repository = mock(ToDoItemRepository.class);
        service = new ToDoServiceImpl(repository);
    }

    @Test
    void testGetAllItemsWithoutFilter() {
        ToDoItem item = createMockItem();
        when(repository.findAll()).thenReturn(List.of(item));

        List<ToDoItem> items = service.getAllItems(Optional.empty());
        assertEquals(1, items.size());
        verify(repository).findAll();
    }

    @Test
    void testGetAllItemsWithStatusFilter() {
        ToDoItem item = createMockItem();
        when(repository.findByStatus(Status.DONE)).thenReturn(List.of(item));

        List<ToDoItem> items = service.getAllItems(Optional.of("done"));
        assertEquals(1, items.size());
        verify(repository).findByStatus(Status.DONE);
    }

    @Test
    void testGetItemByIdSuccess() {
        ToDoItem item = createMockItem();
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        ToDoItem found = service.getItemById(1L);
        assertEquals("Test item", found.getDescription());
    }

    @Test
    void testGetItemByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getItemById(1L));
    }

    @Test
    void testCreateItem() {
        ToDoItem item = createMockItem();
        when(repository.save(item)).thenReturn(item);

        ToDoItem result = service.createItem(item);
        assertEquals("Test item", result.getDescription());
        verify(repository).save(item);
    }

    @Test
    void testUpdateItem() {
        ToDoItem existing = createMockItem();
        existing.setDescription("Old");
        ToDoItem updated = createMockItem();
        updated.setDescription("Updated");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(ToDoItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ToDoItem result = service.updateItem(1L, updated);

        assertEquals("Updated", result.getDescription());
        verify(repository).save(existing);
    }

    @Test
    void testDeleteItem() {
        ToDoItem item = createMockItem();
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        service.deleteItem(1L);

        verify(repository).delete(item);
    }

    // Helper method
    private ToDoItem createMockItem() {
        ToDoItem item = new ToDoItem();
        item.setId(1L);
        item.setDescription("Test item");
        item.setStatus(Status.DONE);
        item.setCreationDatetime(LocalDateTime.now());
        item.setDueDatetime(LocalDateTime.now().plusDays(1));
        item.setDoneDatetime(LocalDateTime.now());
        return item;
    }
}
