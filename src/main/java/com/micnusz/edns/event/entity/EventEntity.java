package com.micnusz.edns.event.entity;

import com.micnusz.edns.event.payload.EventPayload;
import com.micnusz.edns.event.enums.EventType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String recipientId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private EventPayload payload;

    private Instant occurredAt;
}
