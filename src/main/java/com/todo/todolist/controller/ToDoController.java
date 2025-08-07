package com.todo.todolist.controller;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.service.ToDoService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing To-Do items.
 */
@RestController
@RequestMapping("/todos")
public class ToDoController {

  private final ToDoService toDoService;

  /**
   * Constructor for ToDoController.
   *
   * @param toDoService the service to handle todo operations
   */
  @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
                      justification = "ToDoService is an interface used for dependency injection")
  public ToDoController(ToDoService toDoService) {
    this.toDoService = toDoService;
  }

  /**
   * Get all todo items, optionally filtered by status.
   *
   * @param status optional status filter
   * @return list of todo items
   */
  @GetMapping
  public ResponseEntity<List<ToDoItem>> getAllItems(@RequestParam Optional<String> status) {
    List<ToDoItem> items = toDoService.getAllItems(status);

    return ResponseEntity.ok(items);
  }

  /**
   * Get a specific todo item by ID.
   *
   * @param id the item ID
   * @return the todo item
   */
  @GetMapping("/{id}")
  public ResponseEntity<ToDoItem> getItemById(@PathVariable Long id) {
    ToDoItem item = toDoService.getItemById(id);

    return ResponseEntity.ok(item);
  }

  /**
   * Create a new todo item.
   *
   * @param item the item to create
   * @return the created item
   */
  @PostMapping
  public ResponseEntity<ToDoItem> createItem(@RequestBody ToDoItem item) {
    item.setStatus(Status.NOT_DONE);
    ToDoItem created = toDoService.createItem(item);

    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  /**
   * Update an existing todo item.
   *
   * @param id          the item ID
   * @param updatedItem the updated item data
   * @return the updated item
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ToDoItem updatedItem) {
    ToDoItem updated = toDoService.updateItem(id, updatedItem);

    return ResponseEntity.ok(updated);
  }

  /**
   * Mark a todo item as done.
   *
   * @param id the item ID
   * @return the updated item
   */
  @PatchMapping("/{id}/done")
  public ResponseEntity<ToDoItem> markAsDone(@PathVariable Long id) {
    ToDoItem updated = toDoService.markAsDone(id);
    return ResponseEntity.ok(updated);
  }

  /**
   * Mark a todo item as not done.
   *
   * @param id the item ID
   * @return the updated item
   */
  @PatchMapping("/{id}/not-done")
  public ResponseEntity<ToDoItem> markAsNotDone(@PathVariable Long id) {
    ToDoItem updated = toDoService.markAsNotDone(id);
    return ResponseEntity.ok(updated);
  }

  /**
   * Delete a todo item.
   *
   * @param id the item ID
   * @return no content response
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteItem(@PathVariable Long id) {
    toDoService.deleteItem(id);

    return ResponseEntity.noContent().build();
  }
}
