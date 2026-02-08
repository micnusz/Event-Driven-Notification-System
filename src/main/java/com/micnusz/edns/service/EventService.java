package com.micnusz.edns.service;


import com.micnusz.edns.repository.EventRepository;
import com.micnusz.edns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;


}
