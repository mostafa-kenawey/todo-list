package com.todo.todolist.controller;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.service.ToDoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing To-Do items.
 */
@RestController
@RequestMapping("/todos")
public class ToDoController {

    private final ToDoService toDoService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", 
                        justification = "ToDoService is an interface used for dependency injection")
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public ResponseEntity<List<ToDoItem>> getAllItems(@RequestParam Optional<String> status) {
        List<ToDoItem> items = toDoService.getAllItems(status);
        
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoItem> getItemById(@PathVariable Long id) {
        ToDoItem item = toDoService.getItemById(id);
        
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<ToDoItem> createItem(@RequestBody ToDoItem item) {
        item.setStatus(Status.NOT_DONE);
        ToDoItem created = toDoService.createItem(item);
        
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ToDoItem updatedItem) {
        ToDoItem updated = toDoService.updateItem(id, updatedItem);
        
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<ToDoItem> markAsDone(@PathVariable Long id) {
        ToDoItem updated = toDoService.markAsDone(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/not-done")
    public ResponseEntity<ToDoItem> markAsNotDone(@PathVariable Long id) {
        ToDoItem updated = toDoService.markAsNotDone(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        toDoService.deleteItem(id);
        
        return ResponseEntity.noContent().build();
    }
}
