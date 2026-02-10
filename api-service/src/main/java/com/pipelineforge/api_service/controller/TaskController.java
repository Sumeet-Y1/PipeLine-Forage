package com.pipelineforge.api_service.controller;

import com.pipelineforge.api_service.dto.TaskRequest;
import com.pipelineforge.api_service.dto.TaskResponse;
import com.pipelineforge.api_service.model.Task;
import com.pipelineforge.api_service.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId) {
        TaskResponse response = taskService.getTaskById(taskId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) String status) {

        if (status != null) {
            Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status.toUpperCase());
            List<TaskResponse> tasks = taskService.getTasksByStatus(taskStatus);
            return ResponseEntity.ok(tasks);
        }

        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API Service is healthy!");
    }
}