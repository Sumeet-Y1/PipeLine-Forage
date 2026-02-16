package com.pipelineforge.api_service.service;

import com.pipelineforge.api_service.dto.TaskRequest;
import com.pipelineforge.api_service.dto.TaskResponse;
import com.pipelineforge.api_service.model.Task;
import com.pipelineforge.api_service.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final SqsService sqsService;

    public TaskService(TaskRepository taskRepository, SqsService sqsService) {
        this.taskRepository = taskRepository;
        this.sqsService = sqsService;
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        // Create task entity
        Task task = new Task();
        task.setType(request.getType());
        task.setPayload(request.getPayload());
        task.setStatus(Task.TaskStatus.PENDING);

        // Save to database
        Task savedTask = taskRepository.save(task);

        log.info("Task created with ID: {}", savedTask.getId());

        // Send to SQS queue
        try {
            sqsService.sendTaskToQueue(
                    savedTask.getId(),
                    savedTask.getType(),
                    savedTask.getPayload()
            );
        } catch (Exception e) {
            log.error("Failed to send task to queue, but task saved in DB", e);
        }

        return TaskResponse.fromTask(savedTask);
    }

    public TaskResponse getTaskById(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        return TaskResponse.fromTask(task);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());
    }
}