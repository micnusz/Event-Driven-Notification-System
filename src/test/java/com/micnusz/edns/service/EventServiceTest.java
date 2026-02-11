package com.micnusz.edns.service;

import com.micnusz.edns.event.dto.EventRequest;
import com.micnusz.edns.event.dto.EventResponse;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.service.EventService;
import com.micnusz.edns.model.EventEnvelope;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private KafkaTemplate<String, EventEnvelope> kafkaTemplate;

    @InjectMocks
    private EventService eventService;

    @Test
    void shouldCreateEventAndSendToKafka() {
        // Given
        EventRequest request = new EventRequest(
                EventType.TASK_ASSIGNED,
                "user-123",
                Map.of("taskName", "Fix bug", "assignedBy", "Manager")
        );

        // When
        EventResponse response = eventService.createEvent(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEventId()).isNotNull();

        // Verify Kafka send was called
        ArgumentCaptor<EventEnvelope> envelopeCaptor = ArgumentCaptor.forClass(EventEnvelope.class);
        verify(kafkaTemplate).send(
                eq("events"),
                eq("user-123"),
                envelopeCaptor.capture()
        );

        EventEnvelope sentEnvelope = envelopeCaptor.getValue();
        assertThat(sentEnvelope.getEventId()).isEqualTo(response.getEventId());
        assertThat(sentEnvelope.getType()).isEqualTo(EventType.TASK_ASSIGNED);
        assertThat(sentEnvelope.getRecipientId()).isEqualTo("user-123");
        assertThat(sentEnvelope.getPayload()).containsEntry("taskName", "Fix bug");
        assertThat(sentEnvelope.getPayload()).containsEntry("assignedBy", "Manager");
        assertThat(sentEnvelope.getVersion()).isEqualTo(1);
        assertThat(sentEnvelope.getOccurredAt()).isNotNull();
    }

    @Test
    void shouldGenerateUniqueEventIds() {
        // Given
        EventRequest request = new EventRequest(
                EventType.REMINDER,
                "user-456",
                Map.of("text", "Meeting at 3pm")
        );

        // When
        EventResponse response1 = eventService.createEvent(request);
        EventResponse response2 = eventService.createEvent(request);

        // Then
        assertThat(response1.getEventId()).isNotEqualTo(response2.getEventId());
    }

    @Test
    void shouldHandleDifferentEventTypes() {
        // Given
        EventRequest taskRequest = new EventRequest(
                EventType.TASK_COMPLETED,
                "user-789",
                Map.of("taskName", "Deploy app")
        );

        EventRequest alertRequest = new EventRequest(
                EventType.ALERT,
                "user-789",
                Map.of("message", "Server down!")
        );

        // When
        eventService.createEvent(taskRequest);
        eventService.createEvent(alertRequest);

        // Then
        verify(kafkaTemplate, times(2))
                .send(eq("events"), eq("user-789"), any(EventEnvelope.class));
    }
}