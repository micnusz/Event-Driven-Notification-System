package com.micnusz.edns.mapper;


import com.micnusz.edns.dto.EventRequest;
import com.micnusz.edns.dto.EventResponse;
import com.micnusz.edns.model.Event;
import com.micnusz.edns.model.User;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(EventRequest request, User recipient, User actor) {
        Event event = new Event();
        event.setPayload(request.getPayload());
        event.setType(request.getType());
        event.setRecipient(recipient);
        event.setActor(actor);
        return event;
    }

    public EventResponse toDto(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .type(event.getType())
                .payload(event.getPayload())
                .recipientId(event.getRecipient() != null ? event.getRecipient().getId() : null)
                .actorId(event.getActor() != null ? event.getActor().getId() : null)
                .createdAt(event.getCreatedAt())
                .build();
    }
}

