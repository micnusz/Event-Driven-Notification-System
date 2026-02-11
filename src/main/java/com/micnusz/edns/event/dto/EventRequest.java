package com.micnusz.edns.event.dto;

import com.micnusz.edns.event.enums.EventType;
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
            EventType type;
            String recipientId;
            Map<String, Object> payload;

}

