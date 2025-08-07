package com.todo.todolist.service;

import com.todo.todolist.exception.*;
import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.repository.ToDoItemRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ToDoService interface.
 *
 * Handles business logic for managing Todo items, such as filtering by status,
 * handling errors, and updating item states.
 */
@Service
public class ToDoServiceImpl implements ToDoService {

    private final ToDoItemRepository toDoItemRepository;

    public ToDoServiceImpl(ToDoItemRepository toDoItemRepository) {
        this.toDoItemRepository = toDoItemRepository;
    }

    @Override
    public List<ToDoItem> getAllItems(Optional<String> status) {
        if (status.isPresent()) {
            return toDoItemRepository.findByStatus(Status.valueOf(status.get().toUpperCase()));
        }
        return toDoItemRepository.findAll();
    }

    @Override
    public ToDoItem getItemById(Long id) {
        return toDoItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + id));
                
    }

    @Override
    public ToDoItem createItem(ToDoItem item) {
        validateToDoItem(item);

        if (existsByDescriptionAndDueDatetimeAndStatus(item.getDescription(), item.getDueDatetime(), Status.NOT_DONE)) {
            throw new ConflictException("Todo item with the same description and due date already exists.");
        }
        
        return toDoItemRepository.save(item);
    }

    @Override
    public ToDoItem updateItem(Long id, ToDoItem updatedItem) {
        validateToDoItem(updatedItem);

        ToDoItem existingItem = getItemById(id);
        validateOverdue(existingItem);

        boolean duplicateExists = existsByDescriptionAndDueDatetimeAndStatusAndIdNot(
            updatedItem.getDescription(), updatedItem.getDueDatetime(), Status.NOT_DONE, id);

        if (duplicateExists) {
            throw new ConflictException("Todo item with the same description and due date already exists.");
        }

        existingItem.setDescription(updatedItem.getDescription());
        existingItem.setDueDatetime(updatedItem.getDueDatetime());

        return toDoItemRepository.save(existingItem);
    }

    @Override
    public ToDoItem markAsDone(Long id) {
        ToDoItem item = getItemById(id);

        if (item.getStatus() == Status.DONE) {
            throw new ConflictException("Item marked already as done.");
        }

        if (item.getStatus() == Status.OVERDUE) {
            throw new ConflictException("Cannot mark an overdue item as done.");
        }

        item.setStatus(Status.DONE);
        item.setDoneDatetime(LocalDateTime.now());

        return toDoItemRepository.save(item);
    }

    @Override
    public ToDoItem markAsNotDone(Long id) {
        ToDoItem item = getItemById(id);

        if (item.getStatus() == Status.NOT_DONE) {
            throw new ConflictException("Item marked already as not done.");
        }

        if (item.getStatus() == Status.OVERDUE) {
            throw new ConflictException("Cannot mark an overdue item as done.");
        }

        item.setStatus(Status.NOT_DONE);
        item.setDoneDatetime(null);

        return toDoItemRepository.save(item);
    }

    @Override
    public void deleteItem(Long id) {
        ToDoItem item = getItemById(id);
        validateOverdue(item);

        toDoItemRepository.delete(item);
    }

    @Override
    public boolean existsByDescriptionAndDueDatetimeAndStatus(String description, LocalDateTime dueDatetime, Status status) {
        return toDoItemRepository.existsByDescriptionAndDueDatetimeAndStatus(description, dueDatetime, status);
    }

    @Override
    public boolean existsByDescriptionAndDueDatetimeAndStatusAndIdNot(String description, LocalDateTime dueDatetime, Status status, Long id) {
        return toDoItemRepository.existsByDescriptionAndDueDatetimeAndStatusAndIdNot(description, dueDatetime, status, id);
    }

    private void validateToDoItem(ToDoItem item) {
        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Description must not be null or empty.");
        }
        if (item.getDueDatetime() == null || item.getDueDatetime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Due date must be provided and must be in the future.");
        }
    }

    private void validateOverdue(ToDoItem item) {
        if (item.getStatus() == Status.OVERDUE) {
            throw new ForbiddenException("Cannot update or delete a past due item.");
        }
    }
}
