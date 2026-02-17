package com.pipelineforge.api_service.service;

import com.pipelineforge.api_service.dto.TaskRequest;
import com.pipelineforge.api_service.dto.TaskResponse;
import com.pipelineforge.api_service.model.Task;
import com.pipelineforge.api_service.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    @Autowired(required = false)
    private SqsService sqsService;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setType(request.getType());
        task.setPayload(request.getPayload());
        task.setStatus(Task.TaskStatus.PENDING);

        Task savedTask = taskRepository.save(task);
        taskRepository.flush();
        savedTask = taskRepository.findById(savedTask.getId()).orElse(savedTask);

        log.info("Task created with ID: {}", savedTask.getId());

        if (sqsService != null) {
            try {
                sqsService.sendTaskToQueue(
                        savedTask.getId(),
                        savedTask.getType(),
                        savedTask.getPayload()
                );
            } catch (Exception e) {
                log.error("Failed to send task to queue, but task saved in DB", e);
            }
        } else {
            log.info("SQS not configured - skipping queue for local development");
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