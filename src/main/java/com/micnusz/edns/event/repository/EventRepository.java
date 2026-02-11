package com.micnusz.edns.event.repository;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.entity.EventEntity;
import com.micnusz.edns.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID> {
    List<Event> findByRecipientId(String recipientId);
    List<Event> findByType(EventType type);
}
