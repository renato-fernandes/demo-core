package com.rf.sorocaba.demo.core.service;

import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UsersService {

    //TODO: remove this after implement database integration
    private final Map<String, UserResponse> usersMap = new HashMap<>();

    public UserResponse createUser(UserRequest userRequest) {
        UserResponse user = null;
        if(Objects.nonNull(userRequest) &&
                otherEqualUserDoesNotExists(null, userRequest.getUsername(), userRequest.getEmail())){
            user = getUserResponse(userRequest);
            usersMap.put(user.getId(),user);
        }
        return user;
    }

    public UserResponse deleteUser(String id) {
        return usersMap.remove(id);
    }

    public List<UserResponse> getAllUsers() {
        return usersMap.values().stream()
                .toList();
    }

    public UserResponse getUserById(String id) {
        return usersMap.get(id);
    }

    public UserResponse updateUser(String id, UserRequest userRequest) {
        var user = usersMap.get(id);
        UserResponse updatedUser = null;
        if(user != null){
            if(Objects.nonNull(userRequest) &&
                    otherEqualUserDoesNotExists(id, userRequest.getUsername(), userRequest.getEmail())){
                updatedUser = getUserResponse(id, user.getCreatedAt(), OffsetDateTime.now(), userRequest);
                usersMap.put(id, updatedUser);
            }else{
                updatedUser = new UserResponse();
            }
        }
        return updatedUser;
    }

    private UserResponse getUserResponse(UserRequest userRequest) {
        return this.getUserResponse(UUID.randomUUID().toString(), OffsetDateTime.now(), null, userRequest);
    }

    private UserResponse getUserResponse(String id, OffsetDateTime createDate, OffsetDateTime updateDate, UserRequest userRequest) {
        return new UserResponse()
                .id(id)
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .status(userRequest.getStatus())
                .password(userRequest.getPassword())
                .createdAt(createDate)
                .updatedAt(updateDate);
    }

    private boolean otherEqualUserDoesNotExists(String id, String username, String email) {
        return usersMap.values().stream().filter(u -> !Objects.equals(u.getId(), id)).noneMatch(u -> u.getUsername().equals(username)) &&
                usersMap.values().stream().filter(u -> !Objects.equals(u.getId(), id)).noneMatch(u -> u.getEmail().equals(email));

    }
}
