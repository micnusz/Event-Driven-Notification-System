package com.micnusz.edns.notification.builder;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.payload.TaskAssignedPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationBuilderTest {

    @Autowired
    private List<NotificationMessageBuilder> builders;

    @Test
    void shouldHaveBuilderForEveryEventType() {
        Set<EventType> supportedTypes = builders.stream()
                .map(NotificationMessageBuilder::supports)
                .collect(Collectors.toSet());

        assertThat(supportedTypes).containsExactlyInAnyOrder(EventType.values());
    }

    @Test
    void taskAssignedBuilder_shouldBuildCorrectMessage() {
        TaskAssignedPayload payload = TaskAssignedPayload.builder()
                .taskName("Fix bug")
                .assignedBy("John")
                .taskDescription("Critical bug")
                .build();

        NotificationMessageBuilder builder = builders.stream()
                .filter(b -> b.supports() == EventType.TASK_ASSIGNED)
                .findFirst()
                .orElseThrow();

        String message = builder.buildMessage(payload);

        assertThat(message).contains("Fix bug");
        assertThat(message).contains("John");
    }
}