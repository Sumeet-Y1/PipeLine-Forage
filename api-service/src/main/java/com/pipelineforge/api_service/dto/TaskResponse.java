package com.pipelineforge.api_service.dto;

import com.pipelineforge.api_service.model.Task;

import java.time.LocalDateTime;

public class TaskResponse {

    private String taskId;
    private String type;
    private String status;
    private String result;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // Constructors
    public TaskResponse() {}

    public TaskResponse(String taskId, String type, String status, String result,
                        String errorMessage, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.taskId = taskId;
        this.type = type;
        this.status = status;
        this.result = result;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    // Static factory method
    public static TaskResponse fromTask(Task task) {
        TaskResponse response = new TaskResponse();
        response.setTaskId(task.getId());
        response.setType(task.getType());
        response.setStatus(task.getStatus().name());
        response.setResult(task.getResult());
        response.setErrorMessage(task.getErrorMessage());
        response.setCreatedAt(task.getCreatedAt());
        response.setCompletedAt(task.getCompletedAt());
        return response;
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}