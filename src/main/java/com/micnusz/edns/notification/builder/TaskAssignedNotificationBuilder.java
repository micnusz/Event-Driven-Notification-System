package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.TaskAssignedPayload;
import org.springframework.stereotype.Component;

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
        if (!(payload instanceof TaskAssignedPayload taskPayload)) {
            throw new IllegalArgumentException("Invalid payload type for TASK_ASSIGNED");
        }

        return String.format("New task '%s' assigned by %s. %s",
                taskPayload.getTaskName(),
                taskPayload.getAssignedBy(),
                taskPayload.getTaskDescription()
        );
    }
}

