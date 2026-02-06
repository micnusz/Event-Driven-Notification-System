package com.micnusz.edns.service;


import com.micnusz.edns.dto.EventRequest;
import com.micnusz.edns.dto.EventResponse;
import com.micnusz.edns.mapper.EventMapper;
import com.micnusz.edns.model.Event;
import com.micnusz.edns.model.User;
import com.micnusz.edns.repository.EventRepository;
import com.micnusz.edns.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventResponse createEvent(EventRequest request) {

        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new EntityNotFoundException("Recipient not found"));

        User actor = userRepository.findById(request.getActorId())
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));

        Event event = eventMapper.toEntity(request, recipient, actor);
        Event saved = eventRepository.save(event);
        return eventMapper.toDto(saved);
    }
}
