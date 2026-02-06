package com.micnusz.edns.mapper;

import com.micnusz.edns.dto.UserRequest;
import com.micnusz.edns.dto.UserResponse;
import com.micnusz.edns.model.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        return user;
    }

    public UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
