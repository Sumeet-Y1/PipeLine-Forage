package com.pipelineforge.api_service.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskRequest {

    @NotBlank(message = "Task type is required")
    private String type;

    @NotBlank(message = "Payload is required")
    private String payload;

    // Constructors
    public TaskRequest() {}

    public TaskRequest(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}