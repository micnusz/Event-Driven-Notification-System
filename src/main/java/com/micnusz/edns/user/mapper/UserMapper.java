package com.micnusz.edns.user.mapper;

import com.micnusz.edns.user.dto.UserRequest;
import com.micnusz.edns.user.dto.UserResponse;
import com.micnusz.edns.user.entity.UserEntity;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserEntity toEntity(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRequest.getEmail());
        return userEntity;
    }

    public UserResponse toDto(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getCreatedAt()
        );
    }
}
