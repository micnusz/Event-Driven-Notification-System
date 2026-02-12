package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.payload.ReminderPayload;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ReminderNotificationBuilder implements NotificationMessageBuilder {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public EventType supports() {
        return EventType.REMINDER;
    }

    @Override
    public String buildTitle(EventPayload payload) {
        return "Reminder";
    }

    @Override
    public String buildMessage(EventPayload payload) {
        if (!(payload instanceof ReminderPayload reminderPayload)) {
            throw new IllegalArgumentException("Invalid payload type for REMINDER");
        }

        return String.format("%s - Task: %s (Due: %s)",
                reminderPayload.getReminderMessage(),
                reminderPayload.getTaskName(),
                FORMATTER.format(reminderPayload.getReminderTime())
        );
    }
}