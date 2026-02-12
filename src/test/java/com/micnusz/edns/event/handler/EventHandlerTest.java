package com.micnusz.edns.event.handler;

import com.micnusz.edns.event.dto.EventEnvelope;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.service.EventPersistenceService;
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
    @InjectMocks
    private EventHandler eventHandler;


    @Test
    void shouldPersistEventAndDispatchNotification() {
        // given
        EventEnvelope envelope = EventEnvelope.builder()
                .eventId(UUID.randomUUID())
                .type(EventType.TASK_ASSIGNED)
                .recipientId("user-1")
                .occurredAt(Instant.now())
                .version(1)
                .build();

        doNothing().when(eventPersistenceService).save(envelope);

        // when
        eventHandler.handle(envelope);

        // then
        verify(eventPersistenceService).save(envelope);
        verify(notificationApplicationService).handle(any());
    }

    @Test
    void shouldNotDispatchNotificationWhenEventIsDuplicate() {
        // given
        EventEnvelope envelope = EventEnvelope.builder()
                .eventId(UUID.randomUUID())
                .type(EventType.TASK_ASSIGNED)
                .recipientId("user-1")
                .occurredAt(Instant.now())
                .version(1)
                .build();

        doThrow(new DataIntegrityViolationException("duplicate"))
                .when(eventPersistenceService)
                .save(envelope);

        // when
        eventHandler.handle(envelope);

        // then
        verify(eventPersistenceService).save(envelope);
        verify(notificationApplicationService, never()).handle(any());
    }

    @Test
    void shouldPropagateExceptionWhenDispatchFails() {
        // given
        EventEnvelope envelope = EventEnvelope.builder()
                .eventId(UUID.randomUUID())
                .type(EventType.TASK_ASSIGNED)
                .recipientId("user-1")
                .occurredAt(Instant.now())
                .version(1)
                .build();

        doNothing().when(eventPersistenceService).save(envelope);
        doThrow(new RuntimeException("dispatch failed"))
                .when(notificationApplicationService)
                .handle(any());

        // when & then
        assertThatThrownBy(() -> eventHandler.handle(envelope))
                .isInstanceOf(RuntimeException.class);

        verify(eventPersistenceService).save(envelope);
        verify(notificationApplicationService).handle(any());
    }
}



