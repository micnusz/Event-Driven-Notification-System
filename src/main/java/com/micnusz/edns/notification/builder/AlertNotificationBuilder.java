package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.AlertPayload;
import com.micnusz.edns.event.payload.EventPayload;
import org.springframework.stereotype.Component;

@Component
public class AlertNotificationBuilder implements NotificationMessageBuilder {

    @Override
    public EventType supports() {
        return EventType.ALERT;
    }

    @Override
    public String buildTitle(EventPayload payload) {
        if (!(payload instanceof AlertPayload alertPayload)) {
            return "Alert";
        }

        return switch (alertPayload.getAlertLevel().toUpperCase()) {
            case "ERROR" -> "Critical Alert";
            case "WARNING" -> "Warning";
            default -> "Alert";
        };
    }

    @Override
    public String buildMessage(EventPayload payload) {
        if (!(payload instanceof AlertPayload alertPayload)) {
            throw new IllegalArgumentException("Invalid payload type for ALERT");
        }

        return String.format("[%s] %s",
                alertPayload.getAlertLevel(),
                alertPayload.getAlertMessage()
        );
    }
}