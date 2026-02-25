package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.TaskCompletedPayload;
import org.springframework.stereotype.Component;

@Component
public class TaskCompletedNotificationBuilder implements NotificationMessageBuilder<TaskCompletedPayload> {

    @Override
    public EventType supports() {
        return EventType.TASK_COMPLETED;
    }

    @Override
    public String buildTitle(TaskCompletedPayload taskCompletedPayload) {
        return "Task Completed";
    }

    @Override
    public String buildMessage(TaskCompletedPayload taskCompletedPayload) {
        return String.format("'%s' has been completed by %s",
                taskCompletedPayload.taskName(),
                taskCompletedPayload.completedBy()
        );
    }
}