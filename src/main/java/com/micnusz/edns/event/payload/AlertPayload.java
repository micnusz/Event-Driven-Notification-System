package com.micnusz.edns.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertPayload implements EventPayload {
    private String alertMessage;
    private String alertLevel;
    private String relatedTaskId;
}