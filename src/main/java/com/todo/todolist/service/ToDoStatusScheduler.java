package com.todo.todolist.service;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import com.todo.todolist.repository.ToDoItemRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduled service to update status of overdue tasks.
 */
@Service
public class ToDoStatusScheduler {

  private static final Logger logger = LoggerFactory.getLogger(ToDoStatusScheduler.class);

  private final ToDoItemRepository toDoItemRepository;

  public ToDoStatusScheduler(ToDoItemRepository toDoItemRepository) {
    this.toDoItemRepository = toDoItemRepository;
  }

  /**
   * Scheduled method to mark overdue items.
   * Runs every 60 seconds to check for items past their due date.
   */
  @Scheduled(fixedRate = 60000) // Runs every 60 seconds
  @Transactional
  public void markOverdueItems() {
    logger.info("Scheduled task started: checking for overdue items...");

    List<ToDoItem> overdueItems = toDoItemRepository
        .findByStatusAndDueDatetimeBefore(Status.NOT_DONE, LocalDateTime.now());

    if (overdueItems.isEmpty()) {
      logger.info("No overdue items found.");
    } else {
      for (ToDoItem item : overdueItems) {
        logger.info("Marking item {} as OVERDUE", item.getId());
        item.setStatus(Status.OVERDUE);
      }

      toDoItemRepository.saveAll(overdueItems);
      logger.info("Updated {} items to OVERDUE", overdueItems.size());
    }

    logger.info("Scheduled task completed: overdue items processed.");
  }
}
