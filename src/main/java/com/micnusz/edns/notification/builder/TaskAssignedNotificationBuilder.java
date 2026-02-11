package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.notification.NotificationCommand;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TaskAssignedNotificationBuilder implements NotificationMessageBuilder {

    @Override
    public EventType supports() {
        return EventType.TASK_ASSIGNED;
    }

    @Override
    public String buildTitle(NotificationCommand command) {
        return "New Task Assigned";
    }

    @Override
    public String buildMessage(NotificationCommand command) {

        String taskName = getRequired(command.payload(), "taskName");
        String assignedBy = getRequired(command.payload(), "assignedBy");

        return "New task: " + taskName + " assigned by " + assignedBy;
    }

    private String getRequired(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required field: " + key);
        }
        return value.toString();
    }
}

