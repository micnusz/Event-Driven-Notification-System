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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException(request.getEmail());
                });

        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}

