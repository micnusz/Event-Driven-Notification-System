package com.micnusz.edns.event.service;

import com.micnusz.edns.event.entity.EventEntity;
import com.micnusz.edns.event.mapper.EventPersistenceMapper;
import com.micnusz.edns.model.EventEnvelope;
import com.micnusz.edns.event.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EventPersistenceService {

    private final EventRepository eventRepository;
    private final EventPersistenceMapper eventPersistenceMapper;

    @Transactional
    public void save(EventEnvelope envelope) {

        if (eventRepository.existsById(envelope.getEventId())) {
            return;
        }

        EventEntity entity = eventPersistenceMapper.toEntity(envelope);

        eventRepository.save(entity);
    }
}

