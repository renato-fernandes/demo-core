package com.rf.sorocaba.demo.core.mapper;

import com.rf.sorocaba.demo.core.entity.Users;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.model.UserStatus;

public class UserMapper {

    public static Users toEntity(UserRequest request) {
        if (request == null) return null;

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus() == UserStatus.ACTIVE);


        return user;
    }

    public static UserResponse toResponse(Users user) {
        if (user == null) return null;

        return new UserResponse()
                .id(user.getId() != null ? user.getId().toString() : null)
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .status(user.getStatus() != null ? (user.getStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE) : null)
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt() : null)
                .updatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt() : null);
    }
}
