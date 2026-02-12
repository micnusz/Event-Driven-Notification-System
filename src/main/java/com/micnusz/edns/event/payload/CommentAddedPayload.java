package com.micnusz.edns.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddedPayload implements EventPayload {
    private String commentText;
    private String commentAuthor;
    private String taskId;
    private String taskName;
}