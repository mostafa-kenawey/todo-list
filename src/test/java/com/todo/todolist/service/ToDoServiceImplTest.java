package com.todo.todolist.service;

import com.todo.todolist.exception.BadRequestException;
import com.todo.todolist.exception.ConflictException;
import com.todo.todolist.exception.ForbiddenException;
import com.todo.todolist.exception.NotFoundException;
import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.repository.ToDoItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        ToDoItem result = service.getItemById(1L);

        assertNotNull(result);
        assertEquals("Test item", result.getDescription());
    }

    @Test
    void testGetItemByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getItemById(1L));
    }

    @Test
    void testCreateItemSuccess() {
        ToDoItem item = createMockItem();
        when(repository.existsByDescriptionAndDueDatetimeAndStatus(anyString(), any(), any())).thenReturn(false);
        when(repository.save(item)).thenReturn(item);

        ToDoItem result = service.createItem(item);

        assertEquals("Test item", result.getDescription());
        verify(repository).save(item);
    }

    @Test
    void testCreateItemInvalidDescription() {
        ToDoItem item = createMockItem();
        item.setDescription(" ");

        assertThrows(BadRequestException.class, () -> service.createItem(item));
    }

    @Test
    void testCreateItemNullDescription() {
        ToDoItem item = createMockItem();
        item.setDescription(null);

        assertThrows(BadRequestException.class, () -> service.createItem(item));
    }

    @Test
    void testCreateItemInvalidDueDate() {
        ToDoItem item = createMockItem();
        item.setDueDatetime(LocalDateTime.now().minusDays(1));

        assertThrows(BadRequestException.class, () -> service.createItem(item));
    }

    @Test
    void testCreateItemNullDueDate() {
        ToDoItem item = createMockItem();
        item.setDueDatetime(null);

        assertThrows(BadRequestException.class, () -> service.createItem(item));
    }

    @Test
    void testCreateItemDuplicate() {
        ToDoItem item = createMockItem();
        when(repository.existsByDescriptionAndDueDatetimeAndStatus(anyString(), any(), any())).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.createItem(item));
    }

    @Test
    void testUpdateItemSuccess() {
        ToDoItem existing = createMockItem();
        existing.setStatus(Status.NOT_DONE);

        ToDoItem updated = createMockItem();
        updated.setDescription("Updated item");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByDescriptionAndDueDatetimeAndStatus(anyString(), any(), any())).thenReturn(false);
        when(repository.save(any())).thenReturn(existing);

        ToDoItem result = service.updateItem(1L, updated);

        assertEquals("Updated item", result.getDescription());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateItemOverdue() {
        ToDoItem overdue = createMockItem();
        overdue.setStatus(Status.OVERDUE);

        when(repository.findById(1L)).thenReturn(Optional.of(overdue));

        assertThrows(ForbiddenException.class, () -> service.updateItem(1L, overdue));
    }

    @Test
    void testUpdateItemWithNullDescription() {
        ToDoItem existing = createMockItem();
        existing.setStatus(Status.NOT_DONE);

        ToDoItem updated = createMockItem();
        updated.setDescription(null);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(BadRequestException.class, () -> service.updateItem(1L, updated));
    }

    @Test
    void testUpdateItemWithNullDueDate() {
        ToDoItem existing = createMockItem();
        existing.setStatus(Status.NOT_DONE);

        ToDoItem updated = createMockItem();
        updated.setDueDatetime(null);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(BadRequestException.class, () -> service.updateItem(1L, updated));
    }

    @Test
    void testUpdateItemDuplicate() {
        Long itemId = 1L;

        ToDoItem existing = createMockItem();
        existing.setId(itemId);
        existing.setDescription("Original");
        existing.setDueDatetime(LocalDateTime.of(2025, 8, 10, 10, 0));

        ToDoItem updated = createMockItem();
        updated.setDescription("Duplicate"); 
        updated.setDueDatetime(LocalDateTime.of(2025, 8, 10, 10, 0)); 

        when(repository.findById(itemId)).thenReturn(Optional.of(existing));
        when(repository.existsByDescriptionAndDueDatetimeAndStatusAndIdNot(
                updated.getDescription(), updated.getDueDatetime(), Status.NOT_DONE, itemId)).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.updateItem(itemId, updated));
    }

    @Test
    void testDeleteItemSuccess() {
        ToDoItem item = createMockItem();
        item.setStatus(Status.NOT_DONE);

        when(repository.findById(1L)).thenReturn(Optional.of(item));

        service.deleteItem(1L);
        verify(repository).delete(item);
    }

    @Test
    void testDeleteItemOverdue() {
        ToDoItem item = createMockItem();
        item.setStatus(Status.OVERDUE);

        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ForbiddenException.class, () -> service.deleteItem(1L));
    }

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
