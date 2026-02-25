package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.payload.SystemAlertPayload;
import org.springframework.stereotype.Component;

@Component
public class SystemAlertNotificationBuilder implements NotificationMessageBuilder<SystemAlertPayload> {

    @Override
    public EventType supports() {
        return EventType.SYSTEM_ALERT;
    }

    @Override
    public String buildTitle(SystemAlertPayload systemAlertPayload) {
        return switch (systemAlertPayload.severity().toUpperCase()) {
            case "CRITICAL" -> "CRITICAL SYSTEM ALERT";
            case "HIGH" -> "High Priority System Alert";
            case "MEDIUM" -> "System Alert";
            default -> "System Notice";
        };
    }

    @Override
    public String buildMessage(SystemAlertPayload systemAlertPayload) {
        return String.format("[%s] %s - Component: %s",
                systemAlertPayload.severity(),
                systemAlertPayload.systemMessage(),
                systemAlertPayload.affectedComponent()
        );
    }
}