package com.micnusz.edns.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderPayload implements EventPayload {
    private String reminderMessage;
    private String taskName;
    private Instant reminderTime;
}