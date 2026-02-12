package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.dto.EventPayload;
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
    public String buildTitle(EventPayload payload) {
        return "New Task Assigned";
    }

    @Override
    public String buildMessage(EventPayload payload) {
        String taskName = payload.getTaskName();
        String taskDescription = payload.getTaskDescription();

        if (taskName == null || taskName.isBlank()) {
            throw new IllegalArgumentException("Task name is required");
        }

        return String.format("New task: %s - %s", taskName, taskDescription);
    }
}

