package com.micnusz.edns.user.mapper;

import com.micnusz.edns.user.dto.UserRequest;
import com.micnusz.edns.user.dto.UserResponse;
import com.micnusz.edns.user.entity.UserEntity;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserEntity toEntity(UserRequest request) {
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        return user;
    }

    public UserResponse toDto(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
