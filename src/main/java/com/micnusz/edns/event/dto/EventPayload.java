package com.micnusz.edns.event.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPayload {
    private String taskName;
    private String taskDescription;
}
