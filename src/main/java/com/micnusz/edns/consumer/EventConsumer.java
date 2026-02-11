package com.micnusz.edns.consumer;

import com.micnusz.edns.model.Event;
import com.micnusz.edns.model.EventEnvelope;
import com.micnusz.edns.repository.EventRepository;
import com.micnusz.edns.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    private final EventRepository eventRepository;
    private final NotificationService notificationService;

    @KafkaListener(topics = "events", groupId = "notification-service")
    public void consumeEvent(EventEnvelope envelope) {
        log.info("Received event: {}", envelope);

        try {
            Event event = Event.builder()
                    .id(envelope.getEventId())
                    .type(envelope.getType())
                    .recipientId(envelope.getRecipientId())
                    .payload(convertToJson(envelope.getPayload()))
                    .occurredAt(envelope.getOccurredAt())
                    .build();

            eventRepository.save(event);
            log.info("Event saved to database: {}", event.getId());

            notificationService.processEvent(envelope);
        } catch (Exception e) {
            log.error("Failed to process event: {}", envelope.getEventId(), e);
        }
    }

    private String convertToJson(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.error("Failed to convert payload to JSON", e);
            return "{}";
        }
    }
}