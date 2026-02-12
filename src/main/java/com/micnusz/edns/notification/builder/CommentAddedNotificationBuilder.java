package com.micnusz.edns.notification.builder;


import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.CommentAddedPayload;
import com.micnusz.edns.event.payload.EventPayload;
import org.springframework.stereotype.Component;

@Component
public class CommentAddedNotificationBuilder implements NotificationMessageBuilder {

    @Override
    public EventType supports() {
        return EventType.COMMENT_ADDED;
    }

    @Override
    public String buildTitle(EventPayload payload) {
        return "New Comment";
    }

    @Override
    public String buildMessage(EventPayload payload) {
        if (!(payload instanceof CommentAddedPayload commentPayload)) {
            throw new IllegalArgumentException("Invalid payload type for COMMENT_ADDED");
        }

        return String.format("%s commented on '%s': %s",
                commentPayload.getCommentAuthor(),
                commentPayload.getTaskName(),
                commentPayload.getCommentText()
        );
    }
}