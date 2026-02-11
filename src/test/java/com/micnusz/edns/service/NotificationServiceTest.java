package com.micnusz.edns.service;

import com.micnusz.edns.notification.dto.NotificationResponse;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.model.EventEnvelope;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private WebSocketNotificationChannel webSocketNotificationChannel;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldProcessTaskAssignedEvent() {
        // Given
        EventEnvelope event = new EventEnvelope(
                UUID.randomUUID(),
                EventType.TASK_ASSIGNED,
                "user-123",
                Instant.now(),
                Map.of("taskName", "Fix bug", "assignedBy", "Boss"),
                1
        );

        // When
        notificationService.processEvent(event);

        // Then
        ArgumentCaptor<NotificationResponse> captor =
                ArgumentCaptor.forClass(NotificationResponse.class);

        verify(webSocketNotificationChannel).sendToUser(
                eq("user-123"),
                captor.capture()
        );

        NotificationResponse notification = captor.getValue();
        assertThat(notification.getTitle()).isEqualTo("New Task Assigned");
        assertThat(notification.getMessage()).isEqualTo("New task: Fix bug assigned by Boss");
        assertThat(notification.getType()).isEqualTo("TASK_ASSIGNED");
        assertThat(notification.getId()).isNotNull();
    }

    @Test
    void shouldProcessTaskCompletedEvent() {
        // Given
        EventEnvelope event = new EventEnvelope(
                UUID.randomUUID(),
                EventType.TASK_COMPLETED,
                "user-456",
                Instant.now(),
                Map.of("taskName", "Deploy app"),
                1
        );

        // When
        notificationService.processEvent(event);

        // Then
        ArgumentCaptor<NotificationResponse> captor =
                ArgumentCaptor.forClass(NotificationResponse.class);

        verify(webSocketNotificationChannel).sendToUser(
                eq("user-456"),
                captor.capture()
        );

        NotificationResponse notification = captor.getValue();
        assertThat(notification.getTitle()).isEqualTo("Task Completed");
        assertThat(notification.getMessage()).isEqualTo("Task completed: Deploy app");
    }

    @Test
    void shouldProcessReminderEvent() {
        // Given
        EventEnvelope event = new EventEnvelope(
                UUID.randomUUID(),
                EventType.REMINDER,
                "user-789",
                Instant.now(),
                Map.of("text", "Meeting at 3pm"),
                1
        );

        // When
        notificationService.processEvent(event);

        // Then
        ArgumentCaptor<NotificationResponse> captor =
                ArgumentCaptor.forClass(NotificationResponse.class);

        verify(webSocketNotificationChannel).sendToUser(
                eq("user-789"),
                captor.capture()
        );

        NotificationResponse notification = captor.getValue();
        assertThat(notification.getTitle()).isEqualTo("Reminder");
        assertThat(notification.getMessage()).isEqualTo("Meeting at 3pm");
    }

    @Test
    void shouldProcessAlertEvent() {
        // Given
        EventEnvelope event = new EventEnvelope(
                UUID.randomUUID(),
                EventType.ALERT,
                "admin-001",
                Instant.now(),
                Map.of("message", "Server down!"),
                1
        );

        // When
        notificationService.processEvent(event);

        // Then
        ArgumentCaptor<NotificationResponse> captor =
                ArgumentCaptor.forClass(NotificationResponse.class);

        verify(webSocketNotificationChannel).sendToUser(
                eq("admin-001"),
                captor.capture()
        );

        NotificationResponse notification = captor.getValue();
        assertThat(notification.getTitle()).isEqualTo("Alert");
        assertThat(notification.getMessage()).isEqualTo("Server down!");
    }

}