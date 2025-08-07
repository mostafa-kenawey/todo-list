package com.todo.todolist.controller;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.service.ToDoService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/todos")
@Tag(name = "Todo Controller", description = "Todos list management")
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
  @Operation(summary = "Todos list")
  @Parameter(
      name = "status",
      description = "Filter items by status",
      example = "NOT_DONE",
      schema = @Schema(
          type = "string",
          allowableValues = {"NOT_DONE", "DONE", "OVERDUE"}
      )
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    [
                      {
                        "id": 1,
                        "description": "Buy groceries",
                        "status": "NOT_DONE",
                        "creationDatetime": "2025-08-06T20:17:20.04421",
                        "dueDatetime": "2026-08-10T18:00:00",
                        "doneDatetime": null
                      }
                    ]
                  """
              )
          )
      )
  })
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
  @Operation(summary = "Show todo item")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "id": 1,
                      "description": "Buy groceries",
                      "status": "NOT_DONE",
                      "creationDatetime": "2025-08-06T20:11:30.134181",
                      "dueDatetime": "2026-08-10T18:00:00",
                      "doneDatetime": null
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Not Found",
                      "message": "Item not found with id 1",
                      "timestamp": "2025-08-06T20:12:38.771444",
                      "status": 404
                    }
                  """
              )
          )
      )
  })
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
  @Operation(
      summary = "Create a new todo item",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ToDoItem.class),
              examples = @ExampleObject(
                  name = "CreateItemExample",
                  value = "{ \"description\": \"Buy groceries\", \"dueDatetime\": "
                      + "\"2026-08-10T18:00:00\" }"
              )
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Created",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "id": 1,
                      "description": "Buy groceries",
                      "status": "NOT_DONE",
                      "creationDatetime": "2025-08-06T20:11:30.134181",
                      "dueDatetime": "2026-08-10T18:00:00",
                      "doneDatetime": null
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Bad Request",
                      "message": "Due date must be provided and must be in the future.",
                      "timestamp": "2025-08-06T20:29:31.253979",
                      "status": 400
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Already Exists",
                      "message": "Todo item with the same description and due date already exists.",
                      "timestamp": "2025-08-06T20:30:18.041581",
                      "status": 409
                    }
                  """
              )
          )
      )
  })
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
  @Operation(
      summary = "Update todo item",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ToDoItem.class),
              examples = @ExampleObject(
                  name = "CreateItemExample",
                  value = "{ \"description\": \"Buy groceries\", \"dueDatetime\": "
                      + "\"2026-08-10T18:00:00\" }"
              )
          )
      )
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "id": 1,
                      "description": "Buy groceries",
                      "status": "NOT_DONE",
                      "creationDatetime": "2025-08-06T20:11:30.134181",
                      "dueDatetime": "2026-08-10T18:00:00",
                      "doneDatetime": null
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Bad Request",
                      "message": "Due date must be provided and must be in the future.",
                      "timestamp": "2025-08-06T20:29:31.253979",
                      "status": 400
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Not Found",
                      "message": "Item not found with id 1",
                      "timestamp": "2025-08-06T20:29:31.253979",
                      "status": 404
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Already Exists",
                      "message": "Todo item with the same description and due date already exists.",
                      "timestamp": "2025-08-06T20:30:18.041581",
                      "status": 409
                    }
                  """
              )
          )
      )
  })
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
  @Operation(summary = "Mark an item as done")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "id": 1,
                      "description": "Buy groceries",
                      "status": "DONE",
                      "creationDatetime": "2025-08-06T20:11:30.134181",
                      "dueDatetime": "2026-08-10T18:00:00",
                      "doneDatetime": null
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Not Found",
                      "message": "Item not found with id 1",
                      "timestamp": "2025-08-06T20:29:31.253979",
                      "status": 404
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Already Exists",
                      "message": "Item marked already as done.",
                      "timestamp": "2025-08-06T20:59:25.873978",
                      "status": 409
                    }
                  """
              )
          )
      )
  })
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
  @Operation(summary = "Mark an item as Not done")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "id": 1,
                      "description": "Buy groceries",
                      "status": "NOT_DONE",
                      "creationDatetime": "2025-08-06T20:11:30.134181",
                      "dueDatetime": "2026-08-10T18:00:00",
                      "doneDatetime": null
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Not Found",
                      "message": "Item not found with id 1",
                      "timestamp": "2025-08-06T20:29:31.253979",
                      "status": 404
                    }
                  """
              )
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Already Exists",
                      "message": "Item marked already as not done.",
                      "timestamp": "2025-08-06T20:59:25.873978",
                      "status": 409
                    }
                  """
              )
          )
      )
  })
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
  @Operation(summary = "Delete a todo item")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Item deleted"),
      @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                    {
                      "error": "Not Found",
                      "message": "Item not found with id 1",
                      "timestamp": "2025-08-06T20:29:31.253979",
                      "status": 404
                    }
                  """
              )
          )
      )
  })
  public ResponseEntity<?> deleteItem(@PathVariable Long id) {
    toDoService.deleteItem(id);

    return ResponseEntity.noContent().build();
  }
}
