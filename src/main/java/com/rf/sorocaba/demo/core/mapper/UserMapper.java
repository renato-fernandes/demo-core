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

        UserResponse response = new UserResponse();

        if (user.getId() != null)
            response.setId(user.getId().toString());

        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());

        response.setStatus(user.getStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE);

        if (user.getCreatedAt() != null)
            response.setCreatedAt(user.getCreatedAt());

        if (user.getUpdatedAt() != null)
            response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
