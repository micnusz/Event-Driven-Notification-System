package com.micnusz.edns.notification.builder;


import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.CommentAddedPayload;
import com.micnusz.edns.event.payload.EventPayload;
import org.springframework.stereotype.Component;

@Component
public class CommentAddedNotificationBuilder implements NotificationMessageBuilder<CommentAddedPayload> {

    @Override
    public EventType supports() {
        return EventType.COMMENT_ADDED;
    }

    @Override
    public String buildTitle(CommentAddedPayload commentAddedPayload) {
        return "New Comment";
    }

    @Override
    public String buildMessage(CommentAddedPayload commentAddedPayload) {
        return String.format("%s commented on '%s': %s",
                commentAddedPayload.commentAuthor(),
                commentAddedPayload.taskName(),
                commentAddedPayload.commentText()
        );
    }
}