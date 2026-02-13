package com.micnusz.edns.event.dto;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.EventPayload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {
    @NotNull
    EventType type;

    @NotBlank
    String recipientId;

    @NotNull
    EventPayload payload;

    @NotBlank
    private String idempotencyKey;

}

