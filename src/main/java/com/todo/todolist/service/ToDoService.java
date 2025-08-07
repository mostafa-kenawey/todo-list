package com.todo.todolist.service;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Todo items.
 *
 * <p>Defines operations for creating, retrieving, updating and deleting items.
 */
public interface ToDoService {

  List<ToDoItem> getAllItems(Optional<String> status);

  ToDoItem getItemById(Long id);

  ToDoItem createItem(ToDoItem item);

  ToDoItem updateItem(Long id, ToDoItem updatedItem);

  ToDoItem markAsDone(Long id);

  ToDoItem markAsNotDone(Long id);

  void deleteItem(Long id);

  boolean existsByDescriptionAndDueDatetimeAndStatus(String description,
                                                      LocalDateTime dueDatetime,
                                                      Status status);

  boolean existsByDescriptionAndDueDatetimeAndStatusAndIdNot(String description,
                                                              LocalDateTime dueDatetime,
                                                              Status status,
                                                              Long id);
}
