package com.micnusz.edns.controller;

import com.micnusz.edns.event.controller.EventController;
import com.micnusz.edns.event.dto.*;
import com.micnusz.edns.event.entity.EventEntity;
import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.repository.EventRepository;
import com.micnusz.edns.event.service.EventApplicationService;
import com.micnusz.edns.notification.builder.NotificationMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventApplicationService eventApplicationService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private List<NotificationMessageBuilder> notificationBuilders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEvent_shouldReturnCreatedResponse() {
        // given
        EventRequest request = new EventRequest();
        request.setType(EventType.TASK_ASSIGNED);
        request.setRecipientId("user-123");
        request.setIdempotencyKey("abc-123");

        UUID eventId = UUID.randomUUID();
        EventResponse response = new EventResponse();
        response.setEventId(eventId);

        when(eventApplicationService.createEvent(request)).thenReturn(response);

        // when
        ResponseEntity<EventResponse> result = eventController.createEvent(request);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        assertThat(result.getHeaders().getLocation().toString())
                .isEqualTo("/api/events/" + eventId);

        verify(eventApplicationService, times(1)).createEvent(request);
    }

    @Test
    void getRecentEvents_shouldReturnEventHistoryResponses() {
        // given
        EventEntity entity1 = EventEntity.builder()
                .id(UUID.randomUUID())
                .type(EventType.TASK_ASSIGNED)
                .occurredAt(Instant.now())
                .build();

        EventEntity entity2 = EventEntity.builder()
                .id(UUID.randomUUID())
                .type(EventType.TASK_COMPLETED)
                .occurredAt(Instant.now())
                .build();

        when(eventRepository.findAllByOrderByOccurredAtDesc(PageRequest.of(0, 50)))
                .thenReturn(List.of(entity1, entity2));

        // stub static method EventHistoryResponse.from
        try (MockedStatic<EventHistoryResponse> mockedStatic = mockStatic(EventHistoryResponse.class)) {
            EventHistoryResponse response1 = mock(EventHistoryResponse.class);
            EventHistoryResponse response2 = mock(EventHistoryResponse.class);

            mockedStatic.when(() -> EventHistoryResponse.from(entity1, notificationBuilders))
                    .thenReturn(response1);
            mockedStatic.when(() -> EventHistoryResponse.from(entity2, notificationBuilders))
                    .thenReturn(response2);

            // when
            ResponseEntity<List<EventHistoryResponse>> result = eventController.getRecentEvents(50);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).containsExactly(response1, response2);

            verify(eventRepository, times(1)).findAllByOrderByOccurredAtDesc(PageRequest.of(0, 50));
        }
    }
}
