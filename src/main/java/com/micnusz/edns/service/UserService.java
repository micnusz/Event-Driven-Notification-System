package com.micnusz.edns.service;

import com.micnusz.edns.dto.UserRequest;
import com.micnusz.edns.dto.UserResponse;
import com.micnusz.edns.exception.EmailAlreadyExistsException;
import com.micnusz.edns.mapper.UserMapper;
import com.micnusz.edns.model.User;
import com.micnusz.edns.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = UserMapper.toEntity(request);
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }
}

