package com.todo.todolist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Represents a to-do item in the application.
 *
 * <p>Each item contains a description, status, creation date, due date, and
 * optionally a completion date.
 */
@Entity
@Table(name = "todo_items")
public class ToDoItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.NOT_DONE;

  @Column(name = "creation_datetime", nullable = false, updatable = false)
  private LocalDateTime creationDatetime = LocalDateTime.now();

  @Column(name = "due_datetime", nullable = false)
  private LocalDateTime dueDatetime;

  @Column(name = "done_datetime")
  private LocalDateTime doneDatetime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public LocalDateTime getCreationDatetime() {
    return creationDatetime;
  }

  public void setCreationDatetime(LocalDateTime creationDatetime) {
    this.creationDatetime = creationDatetime;
  }

  public LocalDateTime getDueDatetime() {
    return dueDatetime;
  }

  public void setDueDatetime(LocalDateTime dueDatetime) {
    this.dueDatetime = dueDatetime;
  }

  public LocalDateTime getDoneDatetime() {
    return doneDatetime;
  }

  public void setDoneDatetime(LocalDateTime doneDatetime) {
    this.doneDatetime = doneDatetime;
  }
}
