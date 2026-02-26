package com.micnusz.edns.event.handler;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.TaskAssignedPayload;
import com.micnusz.edns.event.service.EventPersistenceService;
import com.micnusz.edns.metrics.EventMetrics;
import com.micnusz.edns.notification.service.NotificationApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventHandlerTest {

    @Mock
    private EventPersistenceService eventPersistenceService;

    @Mock
    private NotificationApplicationService notificationApplicationService;

    @Mock
    private EventMetrics eventMetrics;

    @InjectMocks
    private EventHandler eventHandler;

    @Test
    void shouldPersistEventAndDispatchNotification() {
        TaskAssignedPayload payload = new TaskAssignedPayload("Task 1", "Alice", "Do something", "01-03-2026");
        EventEnvelope envelope = new EventEnvelope(
                UUID.randomUUID(),
                EventType.TASK_ASSIGNED,
                "user-1",
                Instant.now(),
                payload,
                1
        );

        doNothing().when(eventPersistenceService).save(envelope);

        eventHandler.handle(envelope);

        verify(eventPersistenceService).save(envelope);
        verify(notificationApplicationService).handle(any());
        verify(eventMetrics).recordEventProcessed();
        verify(eventMetrics).recordEventProcessingTime(anyLong());
    }

    @Test
    void shouldNotDispatchNotificationWhenEventIsDuplicate() {
        TaskAssignedPayload payload = new TaskAssignedPayload("Task 1", "Alice", "Do something", "01-03-2026");
        EventEnvelope envelope = new EventEnvelope(
                UUID.randomUUID(),
                EventType.TASK_ASSIGNED,
                "user-1",
                Instant.now(),
                payload,
                1
        );

        doThrow(new DataIntegrityViolationException("duplicate"))
                .when(eventPersistenceService)
                .save(envelope);

        eventHandler.handle(envelope);

        verify(eventPersistenceService).save(envelope);
        verify(notificationApplicationService, never()).handle(any());
        verify(eventMetrics).recordEventDuplicated(); // <- teraz jest mock
    }

    @Test
    void shouldPropagateExceptionWhenDispatchFails() {
        TaskAssignedPayload payload = new TaskAssignedPayload("Task 1", "Alice", "Do something", "01-03-2026");
        EventEnvelope envelope = new EventEnvelope(
                UUID.randomUUID(),
                EventType.TASK_ASSIGNED,
                "user-1",
                Instant.now(),
                payload,
                1
        );

        doNothing().when(eventPersistenceService).save(envelope);
        doThrow(new RuntimeException("dispatch failed"))
                .when(notificationApplicationService)
                .handle(any());

        assertThatThrownBy(() -> eventHandler.handle(envelope))
                .isInstanceOf(RuntimeException.class);

        verify(eventPersistenceService).save(envelope);
        verify(notificationApplicationService).handle(any());
    }
}