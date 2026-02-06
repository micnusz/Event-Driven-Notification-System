package com.micnusz.edns.dto;


import com.micnusz.edns.enums.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotNull(message = "Type cannot be null")
    private EventType type;

    @NotNull(message = "Payload cannot be null")
    @Size(min = 1, max = 10000, message = "Payload must be between 1 and 10000 characters")
    private String payload;

    @NotNull(message = "Recipient ID cannot be null")
    private Long recipientId;

    @NotNull(message = "Actor ID cannot be null")
    private Long actorId;
}
