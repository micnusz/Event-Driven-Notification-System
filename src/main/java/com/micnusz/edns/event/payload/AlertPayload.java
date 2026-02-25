package com.micnusz.edns.event.payload;


public record AlertPayload(
        String alertMessage,
        String alertLevel,
        String relatedTaskId
) implements EventPayload {}