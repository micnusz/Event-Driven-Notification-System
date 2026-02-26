package com.micnusz.edns.user.service;

import com.micnusz.edns.user.dto.UserRequest;
import com.micnusz.edns.user.dto.UserResponse;
import com.micnusz.edns.error.exception.EmailAlreadyExistsException;
import com.micnusz.edns.user.mapper.UserMapper;
import com.micnusz.edns.user.entity.UserEntity;
import com.micnusz.edns.user.repository.UserRepository;
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
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException(request.email());
                });

        UserEntity user = userMapper.toEntity(request);
        UserEntity saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}

