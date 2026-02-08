package com.micnusz.edns.model;

import com.micnusz.edns.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String recipientId;

    @Column(columnDefinition = "jsonb")
    private String payload;

    private Instant occurredAt;
}
