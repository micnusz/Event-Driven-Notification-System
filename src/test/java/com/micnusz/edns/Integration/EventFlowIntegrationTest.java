package com.micnusz.edns.Integration;

import com.micnusz.edns.dto.EventRequest;
import com.micnusz.edns.dto.EventResponse;
import com.micnusz.edns.enums.EventType;
import com.micnusz.edns.model.Event;
import com.micnusz.edns.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)
@EmbeddedKafka(
        partitions = 1,
        topics = {"events"},
        brokerProperties = {
                "node.id=1",
                "process.roles=broker,controller",
                "controller.quorum.voters=1@localhost:0",
                "log.cleaner.enable=false",
                "offsets.topic.replication.factor=1",
                "transaction.state.log.replication.factor=1"
        }
)
@DirtiesContext
class EventFlowIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldCreateEventAndSaveToDatabase() {
        // Given
        RestClient restClient = RestClient.create("http://localhost:" + port);

        EventRequest request = new EventRequest(
                EventType.TASK_ASSIGNED,
                "user-123",
                Map.of("taskName", "Test Task", "assignedBy", "Boss")
        );

        // When
        EventResponse response = restClient
                .post()
                .uri("/api/events")
                .body(request)
                .retrieve()
                .body(EventResponse.class);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEventId()).isNotNull();

        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Event> event = eventRepository.findById(response.getEventId());

                    assertThat(event).isPresent();
                    assertThat(event.get().getType()).isEqualTo(EventType.TASK_ASSIGNED);
                    assertThat(event.get().getRecipientId()).isEqualTo("user-123");
                    assertThat(event.get().getPayload()).contains("Test Task");
                });
    }

    @Test
    void shouldHandleMultipleEvents() {
        // Given
        RestClient restClient = RestClient.create("http://localhost:" + port);

        EventRequest request1 = new EventRequest(
                EventType.TASK_ASSIGNED,
                "user-1",
                Map.of("taskName", "Task 1", "assignedBy", "Boss")
        );

        EventRequest request2 = new EventRequest(
                EventType.TASK_COMPLETED,
                "user-2",
                Map.of("taskName", "Task 2")
        );

        // When
        EventResponse response1 = restClient.post()
                .uri("/api/events")
                .body(request1)
                .retrieve()
                .body(EventResponse.class);

        EventResponse response2 = restClient.post()
                .uri("/api/events")
                .body(request2)
                .retrieve()
                .body(EventResponse.class);

        // Then
        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(eventRepository.findById(response1.getEventId())).isPresent();
            assertThat(eventRepository.findById(response2.getEventId())).isPresent();
        });
    }

    @Test
    void shouldProcessDifferentEventTypes() {
        // Given
        RestClient restClient = RestClient.create("http://localhost:" + port);

        // Test all event types
        EventRequest taskAssigned = new EventRequest(
                EventType.TASK_ASSIGNED,
                "user-1",
                Map.of("taskName", "Fix bug", "assignedBy", "Manager")
        );

        EventRequest taskCompleted = new EventRequest(
                EventType.TASK_COMPLETED,
                "user-2",
                Map.of("taskName", "Deploy app")
        );

        EventRequest reminder = new EventRequest(
                EventType.REMINDER,
                "user-3",
                Map.of("text", "Meeting at 3pm")
        );

        EventRequest alert = new EventRequest(
                EventType.ALERT,
                "admin",
                Map.of("message", "Server down!")
        );

        // When
        EventResponse r1 = restClient.post().uri("/api/events").body(taskAssigned).retrieve().body(EventResponse.class);
        EventResponse r2 = restClient.post().uri("/api/events").body(taskCompleted).retrieve().body(EventResponse.class);
        EventResponse r3 = restClient.post().uri("/api/events").body(reminder).retrieve().body(EventResponse.class);
        EventResponse r4 = restClient.post().uri("/api/events").body(alert).retrieve().body(EventResponse.class);

        // Then
        await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            Optional<Event> event1 = eventRepository.findById(r1.getEventId());
            Optional<Event> event2 = eventRepository.findById(r2.getEventId());
            Optional<Event> event3 = eventRepository.findById(r3.getEventId());
            Optional<Event> event4 = eventRepository.findById(r4.getEventId());

            assertThat(event1).isPresent();
            assertThat(event1.get().getType()).isEqualTo(EventType.TASK_ASSIGNED);

            assertThat(event2).isPresent();
            assertThat(event2.get().getType()).isEqualTo(EventType.TASK_COMPLETED);

            assertThat(event3).isPresent();
            assertThat(event3.get().getType()).isEqualTo(EventType.REMINDER);

            assertThat(event4).isPresent();
            assertThat(event4.get().getType()).isEqualTo(EventType.ALERT);
        });
    }
}