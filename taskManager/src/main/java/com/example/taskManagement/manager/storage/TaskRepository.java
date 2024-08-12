package com.example.taskManagement.manager.storage;

import com.example.taskManagement.manager.entities.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "from Task t where t.creator.id=:idCreator")
    List<Task> findByCreatorId(Long idCreator, Pageable pageable);

    @Query(value = "from Task t where t.performer.id=:idPerformer")
    List<Task> findByPerformerId(Long idPerformer, Pageable pageable);
}
