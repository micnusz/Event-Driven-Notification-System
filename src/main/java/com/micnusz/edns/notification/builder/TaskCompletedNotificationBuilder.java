package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.payload.TaskCompletedPayload;
import org.springframework.stereotype.Component;

@Component
public class TaskCompletedNotificationBuilder implements NotificationMessageBuilder {

    @Override
    public EventType supports() {
        return EventType.TASK_COMPLETED;
    }

    @Override
    public String buildTitle(EventPayload payload) {
        return "Task Completed";
    }

    @Override
    public String buildMessage(EventPayload payload) {
        if (!(payload instanceof TaskCompletedPayload taskPayload)) {
            throw new IllegalArgumentException("Invalid payload type for TASK_COMPLETED");
        }

        return String.format("'%s' has been completed by %s",
                taskPayload.getTaskName(),
                taskPayload.getCompletedBy()
        );
    }
}