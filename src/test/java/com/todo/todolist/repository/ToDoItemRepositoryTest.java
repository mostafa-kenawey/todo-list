package com.todo.todolist.repository;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ToDoItemRepositoryTest {

    @Autowired
    private ToDoItemRepository repository;

    private ToDoItem createSampleItem(String description, Status status) {
        ToDoItem item = new ToDoItem();
        item.setDescription(description);
        item.setStatus(status);
        item.setCreationDatetime(LocalDateTime.now());
        item.setDueDatetime(LocalDateTime.now().plusDays(1));
        return repository.save(item);
    }

    @Test
    @DisplayName("Should save and retrieve ToDoItem by ID")
    void testSaveAndFindById() {
        ToDoItem saved = createSampleItem("Test task", Status.NOT_DONE);

        Optional<ToDoItem> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Test task");
        assertThat(found.get().getStatus()).isEqualTo(Status.NOT_DONE);
    }

    @Test
    @DisplayName("Should retrieve all ToDoItems by status")
    void testFindByStatus() {
        createSampleItem("Task 1", Status.NOT_DONE);
        createSampleItem("Task 2", Status.DONE);

        List<ToDoItem> notDoneItems = repository.findByStatus(Status.NOT_DONE);

        assertThat(notDoneItems).hasSize(1);
        assertThat(notDoneItems.get(0).getStatus()).isEqualTo(Status.NOT_DONE);
    }

    @Test
    @DisplayName("Should delete ToDoItem by ID")
    void testDeleteById() {
        ToDoItem item = createSampleItem("Task to delete", Status.NOT_DONE);
        Long id = item.getId();

        repository.deleteById(id);

        Optional<ToDoItem> deleted = repository.findById(id);
        assertThat(deleted).isNotPresent();
    }
}
