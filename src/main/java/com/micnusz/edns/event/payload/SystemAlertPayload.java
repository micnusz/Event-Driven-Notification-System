package com.micnusz.edns.event.payload;


public record SystemAlertPayload (
         String systemMessage,
         String severity,
         String affectedComponent
) implements EventPayload {
}