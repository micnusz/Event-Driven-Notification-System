package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.payload.SystemAlertPayload;
import org.springframework.stereotype.Component;

@Component
public class SystemAlertNotificationBuilder implements NotificationMessageBuilder {

    @Override
    public EventType supports() {
        return EventType.SYSTEM_ALERT;
    }

    @Override
    public String buildTitle(EventPayload payload) {
        if (!(payload instanceof SystemAlertPayload systemPayload)) {
            return "System Alert";
        }

        return switch (systemPayload.getSeverity().toUpperCase()) {
            case "CRITICAL" -> "CRITICAL SYSTEM ALERT";
            case "HIGH" -> "High Priority System Alert";
            case "MEDIUM" -> "System Alert";
            default -> "System Notice";
        };
    }

    @Override
    public String buildMessage(EventPayload payload) {
        if (!(payload instanceof SystemAlertPayload systemPayload)) {
            throw new IllegalArgumentException("Invalid payload type for SYSTEM_ALERT");
        }

        return String.format("[%s] %s - Component: %s",
                systemPayload.getSeverity(),
                systemPayload.getSystemMessage(),
                systemPayload.getAffectedComponent()
        );
    }
}