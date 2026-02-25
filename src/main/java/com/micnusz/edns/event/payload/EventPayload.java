package com.micnusz.edns.event.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Embeddable;
import lombok.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "payloadType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TaskAssignedPayload.class, name = "TASK_ASSIGNED"),
        @JsonSubTypes.Type(value = CommentAddedPayload.class, name = "COMMENT_ADDED"),
        @JsonSubTypes.Type(value = TaskCompletedPayload.class, name = "TASK_COMPLETED"),
        @JsonSubTypes.Type(value = ReminderPayload.class, name = "REMINDER"),
        @JsonSubTypes.Type(value = AlertPayload.class, name = "ALERT"),
        @JsonSubTypes.Type(value = SystemAlertPayload.class, name = "SYSTEM_ALERT")
})
public sealed interface EventPayload permits AlertPayload, CommentAddedPayload, ReminderPayload, SystemAlertPayload, TaskAssignedPayload, TaskCompletedPayload {
}