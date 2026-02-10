package com.micnusz.edns.Service;

import com.micnusz.edns.dto.NotificationResponse;
import com.micnusz.edns.enums.EventType;
import com.micnusz.edns.model.EventEnvelope;
import com.micnusz.edns.service.NotificationService;
import com.micnusz.edns.service.WebSocketNotificationChannel;
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
                Instant.now().toString(),
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
        assertThat(notification.title()).isEqualTo("New Task Assigned");
        assertThat(notification.message()).isEqualTo("New task: Fix bug assigned by Boss");
        assertThat(notification.type()).isEqualTo("TASK_ASSIGNED");
        assertThat(notification.id()).isNotNull();
    }

    @Test
    void shouldProcessTaskCompletedEvent() {
        // Given
        EventEnvelope event = new EventEnvelope(
                UUID.randomUUID(),
                EventType.TASK_COMPLETED,
                "user-456",
                Instant.now().toString(),
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
        assertThat(notification.title()).isEqualTo("Task Completed");
        assertThat(notification.message()).isEqualTo("Task completed: Deploy app");
    }

    @Test
    void shouldProcessReminderEvent() {
        // Given
        EventEnvelope event = new EventEnvelope(
                UUID.randomUUID(),
                EventType.REMINDER,
                "user-789",
                Instant.now().toString(),
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
        assertThat(notification.title()).isEqualTo("Reminder");
        assertThat(notification.message()).isEqualTo("Meeting at 3pm");
    }

    @Test
    void shouldProcessAlertEvent() {
        // Given
        EventEnvelope event = new EventEnvelope(
                UUID.randomUUID(),
                EventType.ALERT,
                "admin-001",
                Instant.now().toString(),
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
        assertThat(notification.title()).isEqualTo("Alert");
        assertThat(notification.message()).isEqualTo("Server down!");
    }

}