package com.micnusz.edns.service;

import com.micnusz.edns.model.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public void processEvent(EventEnvelope event) {
        log.info("🔔 NOTIFICATION for recipient: {}", event.recipientId());
        log.info("   Type: {}", event.type());
        log.info("   Payload: {}", event.payload());

        String message = buildMessage(event);
        log.info("   Message: {}", message);
    }

    private String buildMessage(EventEnvelope event) {
        return switch (event.type()) {
            case TASK_ASSIGNED -> "New task: " + event.payload().get("taskName");
            case TASK_COMPLETED -> "Task completed: " + event.payload().get("taskName");
            default -> "Event: " + event.type();
        };
    }
}
