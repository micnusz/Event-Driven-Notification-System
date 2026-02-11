package com.micnusz.edns.service;

import com.micnusz.edns.dto.NotificationResponse;
import com.micnusz.edns.model.EventEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final WebSocketNotificationChannel webSocketNotificationChannel;

    public void processEvent(EventEnvelope event) {
        log.info("🔔 NOTIFICATION for recipient: {}", event.getRecipientId());
        log.info("   Type: {}", event.getType());
        log.info("   Payload: {}", event.getPayload());

        String title = buildTitle(event);
        String message = buildMessage(event);

        log.info("   Message: {}", message);

        NotificationResponse notification = NotificationResponse.from(event, title, message);
        webSocketNotificationChannel.sendToUser(event.getRecipientId(), notification);
    }


    private String buildTitle(EventEnvelope event) {
        return switch (event.getType()) {
            case TASK_ASSIGNED -> "New Task Assigned";
            case TASK_COMPLETED -> "Task Completed";
            case REMINDER -> "Reminder";
            case ALERT -> "Alert";
            default -> "Notification";
        };
    }

    private String buildMessage(EventEnvelope event) {
        return switch (event.getType()) {
            case TASK_ASSIGNED -> "New task: " + event.getPayload().get("taskName")
                    + " assigned by " + event.getPayload().get("assignedBy");
            case TASK_COMPLETED -> "Task completed: " + event.getPayload().get("taskName");
            case REMINDER -> (String) event.getPayload().get("text");
            case ALERT -> (String) event.getPayload().get("message");
            default -> "Event: " + event.getType();
        };
    }
}
