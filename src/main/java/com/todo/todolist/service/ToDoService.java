package com.todo.todolist.service;

import com.todo.todolist.model.ToDoItem;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing ToDo items.
 *
 * Defines operations for creating, retrieving, updating and deleting items.
 */
public interface ToDoService {

    List<ToDoItem> getAllItems(Optional<String> status);
    
    ToDoItem getItemById(Long id);
    
    ToDoItem createItem(ToDoItem item);
    
    ToDoItem updateItem(Long id, ToDoItem updatedItem);
    
    void deleteItem(Long id);
}
