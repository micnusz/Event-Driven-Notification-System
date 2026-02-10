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
        log.info("🔔 NOTIFICATION for recipient: {}", event.recipientId());
        log.info("   Type: {}", event.type());
        log.info("   Payload: {}", event.payload());

        String title = buildTitle(event);
        String message = buildMessage(event);

        log.info("   Message: {}", message);

        NotificationResponse notification = NotificationResponse.from(event, title, message);
        webSocketNotificationChannel.sendToUser(event.recipientId(), notification);
    }


    private String buildTitle(EventEnvelope event) {
        return switch (event.type()) {
            case TASK_ASSIGNED -> "New Task Assigned";
            case TASK_COMPLETED -> "Task Completed";
            case REMINDER -> "Reminder";
            case ALERT -> "Alert";
            default -> "Notification";
        };
    }

    private String buildMessage(EventEnvelope event) {
        return switch (event.type()) {
            case TASK_ASSIGNED -> "New task: " + event.payload().get("taskName")
                    + " assigned by " + event.payload().get("assignedBy");
            case TASK_COMPLETED -> "Task completed: " + event.payload().get("taskName");
            case REMINDER -> (String) event.payload().get("text");
            case ALERT -> (String) event.payload().get("message");
            default -> "Event: " + event.type();
        };
    }
}
