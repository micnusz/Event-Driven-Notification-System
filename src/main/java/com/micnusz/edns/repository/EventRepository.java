package com.micnusz.edns.repository;

import com.micnusz.edns.enums.EventType;
import com.micnusz.edns.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByRecipientId(String recipientId);
    List<Event> findByType(EventType type);
}
