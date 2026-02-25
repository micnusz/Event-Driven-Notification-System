package com.micnusz.edns.event.payload;


public record CommentAddedPayload (
        String commentText,
        String commentAuthor,
        String taskId,
        String taskName
) implements EventPayload { }