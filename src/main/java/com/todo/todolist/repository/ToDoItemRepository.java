package com.todo.todolist.repository;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for ToDoItem entity.
 * Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {

  /**
   * Find all items by status.
   *
   * @param status the status to filter by
   * @return list of matching ToDoItems
   */
  List<ToDoItem> findByStatus(Status status);

  boolean existsByDescriptionAndDueDatetimeAndStatus(String description,
                                                      LocalDateTime dueDatetime,
                                                      Status status);

  boolean existsByDescriptionAndDueDatetimeAndStatusAndIdNot(String description,
                                                              LocalDateTime dueDatetime,
                                                              Status status,
                                                              Long id);
}
