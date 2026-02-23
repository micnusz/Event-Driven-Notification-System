package com.micnusz.edns.kafka.config;

import com.micnusz.edns.event.dto.EventEnvelope;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import tools.jackson.databind.ObjectMapper;

public class EventEnvelopeDeserializer implements Deserializer<EventEnvelope> {

    private final ObjectMapper objectMapper;

    public EventEnvelopeDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public EventEnvelope deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, EventEnvelope.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing EventEnvelope", e);
        }
    }
}