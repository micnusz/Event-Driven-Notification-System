package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.TaskAssignedPayload;
import org.springframework.stereotype.Component;

@Component
public class TaskAssignedNotificationBuilder implements NotificationMessageBuilder<TaskAssignedPayload> {

    @Override
    public EventType supports() {
        return EventType.TASK_ASSIGNED;
    }

    @Override
    public String buildTitle(TaskAssignedPayload payload) {
        return "New Task Assigned";
    }

    @Override
    public String buildMessage(TaskAssignedPayload payload) {
        return String.format("New task '%s' assigned by %s. %s",
                payload.taskName(),
                payload.assignedBy(),
                payload.taskDescription());
    }
}

