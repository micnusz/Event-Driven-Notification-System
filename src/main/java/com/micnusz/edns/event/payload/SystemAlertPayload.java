package com.micnusz.edns.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemAlertPayload implements EventPayload {
    private String systemMessage;
    private String severity;
    private String affectedComponent;
}