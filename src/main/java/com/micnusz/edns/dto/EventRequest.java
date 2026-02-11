package com.micnusz.edns.dto;

import com.micnusz.edns.enums.EventType;
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

