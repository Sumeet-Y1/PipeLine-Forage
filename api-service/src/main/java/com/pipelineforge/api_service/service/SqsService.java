package com.pipelineforge.api_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsService {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

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