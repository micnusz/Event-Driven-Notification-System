package com.micnusz.edns.kafka.config;

import com.micnusz.edns.event.dto.EventEnvelope;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import tools.jackson.databind.ObjectMapper;

public class EventEnvelopeSerializer implements Serializer<EventEnvelope> {

    private final ObjectMapper objectMapper;

    public EventEnvelopeSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(String topic, EventEnvelope data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing EventEnvelope", e);
        }
    }
}