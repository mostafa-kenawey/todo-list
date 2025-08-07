package com.todo.todolist.service;

import com.todo.todolist.exception.ResourceNotFoundException;
import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.repository.ToDoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ToDoService interface.
 *
 * Handles business logic for managing ToDo items, such as filtering by status,
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
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));
    }

    @Override
    public ToDoItem createItem(ToDoItem item) {
        return toDoItemRepository.save(item);
    }

    @Override
    public ToDoItem updateItem(Long id, ToDoItem updatedItem) {
        ToDoItem existingItem = getItemById(id);

        existingItem.setDescription(updatedItem.getDescription());
        existingItem.setStatus(updatedItem.getStatus());
        existingItem.setDueDatetime(updatedItem.getDueDatetime());
        existingItem.setDoneDatetime(updatedItem.getDoneDatetime());

        return toDoItemRepository.save(existingItem);
    }

    @Override
    public void deleteItem(Long id) {
        ToDoItem item = getItemById(id);
        toDoItemRepository.delete(item);
    }
}
