package com.example.taskManagement.manager.storage;

import com.example.taskManagement.manager.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks WHERE creator_id=:idCreator",
            nativeQuery = true)
    List<Task> findByCreatorId(Long idCreator, Pageable pageable);

    @Query(value = "SELECT * FROM tasks WHERE performer_id=:idPerformer",
            nativeQuery = true)
    List<Task> findByPerformerId(Long idPerformer, Pageable pageable);
}
