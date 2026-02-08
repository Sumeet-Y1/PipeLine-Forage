package com.pipelineforge.api_service.dto;

import com.pipelineforge.api_service.model.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    private String taskId;
    private String type;
    private String status;
    private String result;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public static TaskResponse fromTask(Task task) {
        return TaskResponse.builder()
                .taskId(task.getId())
                .type(task.getType())
                .status(task.getStatus().name())
                .result(task.getResult())
                .errorMessage(task.getErrorMessage())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }
}