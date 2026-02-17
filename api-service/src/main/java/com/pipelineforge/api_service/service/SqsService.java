package com.pipelineforge.api_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@Lazy
public class SqsService {

    private static final Logger log = LoggerFactory.getLogger(SqsService.class);

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public SqsService(SqsClient sqsClient, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    public void sendTaskToQueue(String taskId, String type, String payload) {
        try {
            // Create message body
            Map<String, String> message = new HashMap<>();
            message.put("taskId", taskId);
            message.put("type", type);
            message.put("payload", payload);

            String messageBody = objectMapper.writeValueAsString(message);

            // Send message to SQS
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            SendMessageResponse response = sqsClient.sendMessage(sendMessageRequest);

            log.info("Message sent to SQS. MessageId: {}, TaskId: {}",
                    response.messageId(), taskId);

        } catch (Exception e) {
            log.error("Error sending message to SQS for taskId: {}", taskId, e);
            throw new RuntimeException("Failed to send task to queue", e);
        }
    }
}