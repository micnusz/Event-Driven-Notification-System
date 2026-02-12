package com.micnusz.edns.event.dto;

import com.micnusz.edns.event.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {
    @NotBlank
    EventType type;
    @NotBlank
    String recipientId;
    EventPayload payload;

}

