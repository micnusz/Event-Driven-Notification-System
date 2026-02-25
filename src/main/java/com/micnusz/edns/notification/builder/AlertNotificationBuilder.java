package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.AlertPayload;
import com.micnusz.edns.event.payload.EventPayload;
import org.springframework.stereotype.Component;

@Component
public class AlertNotificationBuilder implements NotificationMessageBuilder<AlertPayload> {

    @Override
    public EventType supports() {
        return EventType.ALERT;
    }

    @Override
    public String buildTitle(AlertPayload payload) {
        return switch (payload.alertLevel().toUpperCase()) {
            case "ERROR" -> "Critical Alert";
            case "WARNING" -> "Warning";
            default -> "New Alert";
        };
    }

    @Override
    public String buildMessage(AlertPayload payload) {
        return String.format("[%s] %s",
                payload.alertLevel(),
                payload.alertMessage()
        );
    }
}