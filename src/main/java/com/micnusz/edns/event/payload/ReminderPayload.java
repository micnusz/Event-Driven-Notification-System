package com.micnusz.edns.event.payload;


import java.time.Instant;


public record ReminderPayload (
     String reminderMessage,
     String taskName,
     Instant reminderTime
) implements EventPayload { }