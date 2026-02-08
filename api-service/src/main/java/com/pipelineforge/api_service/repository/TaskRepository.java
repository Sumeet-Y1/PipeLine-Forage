package com.pipelineforge.api_service.repository;

import com.pipelineforge.api_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByStatusOrderByCreatedAtDesc(Task.TaskStatus status);
}