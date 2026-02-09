package com.pipelineforge.api_service.service;

import com.pipelineforge.api_service.dto.TaskRequest;
import com.pipelineforge.api_service.dto.TaskResponse;
import com.pipelineforge.api_service.model.Task;
import com.pipelineforge.api_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final SqsService sqsService;

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
            // Task is saved but queue failed - worker can still pick it up later
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