package com.todo.todolist.repository;

import com.todo.todolist.model.Status;
import com.todo.todolist.model.ToDoItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback
class ToDoItemRepositoryTest {

    @Autowired
    private ToDoItemRepository repository;
    
    @Autowired
    private TestEntityManager entityManager;

    private ToDoItem createSampleItem(String description, Status status, LocalDateTime dueDatetime) {
        ToDoItem item = new ToDoItem();
        item.setDescription(description);
        item.setStatus(status);
        item.setCreationDatetime(LocalDateTime.now());
        item.setDueDatetime(dueDatetime);
        return repository.save(item);
    }

    @Test
    @DisplayName("Should save and retrieve ToDoItem by ID")
    void testSaveAndFindById() {
        ToDoItem saved = createSampleItem("Test task", Status.NOT_DONE, LocalDateTime.now().plusDays(1));

        Optional<ToDoItem> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Test task");
        assertThat(found.get().getStatus()).isEqualTo(Status.NOT_DONE);
    }

    @Test
    @DisplayName("Should retrieve all ToDoItems by status")
    void testFindByStatus() {
        createSampleItem("Task 1", Status.NOT_DONE, LocalDateTime.now().plusDays(1));
        createSampleItem("Task 2", Status.DONE, LocalDateTime.now().plusDays(2));

        List<ToDoItem> notDoneItems = repository.findByStatus(Status.NOT_DONE);
        List<ToDoItem> doneItems = repository.findByStatus(Status.DONE);

        assertThat(notDoneItems).hasSize(1);
        assertThat(notDoneItems.get(0).getDescription()).isEqualTo("Task 1");

        assertThat(doneItems).hasSize(1);
        assertThat(doneItems.get(0).getDescription()).isEqualTo("Task 2");
    }

    @Test
    @DisplayName("Should delete ToDoItem by ID")
    void testDeleteById() {
        ToDoItem item = createSampleItem("Task to delete", Status.NOT_DONE, LocalDateTime.now().plusDays(1));
        Long id = item.getId();

        repository.deleteById(id);

        Optional<ToDoItem> deleted = repository.findById(id);
        assertThat(deleted).isNotPresent();
    }

    @Test
    @DisplayName("Should check existence by description and dueDatetime")
    void testexistsByDescriptionAndDueDatetimeAndStatus() {
        LocalDateTime due = LocalDateTime.of(2025, 1, 1, 12, 0, 0).truncatedTo(ChronoUnit.SECONDS);
        
        ToDoItem item = new ToDoItem();
        item.setDescription("Unique Task");
        item.setStatus(Status.NOT_DONE);
        item.setCreationDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        item.setDueDatetime(due);
        
        entityManager.persistAndFlush(item);
        entityManager.clear();

        boolean exists = repository.existsByDescriptionAndDueDatetimeAndStatus("Unique Task", due, Status.NOT_DONE);
        boolean notExists = repository.existsByDescriptionAndDueDatetimeAndStatus("Other Task", due, Status.NOT_DONE);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should check existence by description and dueDatetime excluding current ID")
    void testexistsByDescriptionAndDueDatetimeAndStatusAndIdNot() {
        LocalDateTime due = LocalDateTime.of(2025, 1, 2, 14, 0, 0).truncatedTo(ChronoUnit.SECONDS);
        
        ToDoItem item1 = new ToDoItem();
        item1.setDescription("Task to update");
        item1.setStatus(Status.NOT_DONE);
        item1.setCreationDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        item1.setDueDatetime(due);
        entityManager.persistAndFlush(item1);
        
        ToDoItem item2 = new ToDoItem();
        item2.setDescription("Another Task");
        item2.setStatus(Status.NOT_DONE);
        item2.setCreationDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        item2.setDueDatetime(due);
        entityManager.persistAndFlush(item2);
        
        entityManager.clear();

        boolean exists = repository.existsByDescriptionAndDueDatetimeAndStatusAndIdNot("Another Task", due, Status.NOT_DONE, item1.getId());
        boolean notExists = repository.existsByDescriptionAndDueDatetimeAndStatusAndIdNot("Task to update 2", due, Status.NOT_DONE, item1.getId());

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testFindByStatusAndDueDatetimeBefore() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        ToDoItem overdueItem = createSampleItem("Past Task", Status.NOT_DONE, past);
        ToDoItem futureItem = createSampleItem("Future Task", Status.NOT_DONE, future);
        ToDoItem donePastItem = createSampleItem("Done Task", Status.DONE, past);

        repository.save(overdueItem);
        repository.save(futureItem);
        repository.save(donePastItem);

        List<ToDoItem> result = repository.findByStatusAndDueDatetimeBefore(Status.NOT_DONE, LocalDateTime.now());
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Past Task");
    }
}
