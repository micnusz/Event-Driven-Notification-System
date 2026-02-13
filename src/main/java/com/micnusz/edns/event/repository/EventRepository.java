package com.micnusz.edns.event.repository;

import com.micnusz.edns.event.enums.EventType;
import com.micnusz.edns.event.entity.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID> {
    List<EventEntity> findByRecipientId(String recipientId);
    List<EventEntity> findByType(EventType type);
    List<EventEntity> findAllByOrderByOccurredAtDesc(Pageable pageable);
}
