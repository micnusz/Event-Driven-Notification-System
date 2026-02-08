package com.micnusz.edns.dto;

import com.micnusz.edns.enums.EventType;

import java.util.Map;

public record EventRequest(
        EventType type,
        String recipientId,
        Map<String, Object> payload
) {}
