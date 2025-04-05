package com.rf.sorocaba.demo.core.controller;

import com.rf.sorocaba.demo.core.api.UsersApi;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class UsersController implements UsersApi {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) {
        UserResponse newUser = usersService.createUser(userRequest);
        return Objects.nonNull(newUser) ?
                ResponseEntity.ok(newUser) :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(String id) {
        UserResponse deletedUser = usersService.deleteUser(id);
        return Objects.nonNull(deletedUser) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> allUsers = usersService.getAllUsers();
        return Objects.nonNull(allUsers) ? ResponseEntity.ok(allUsers) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(String id) {
        UserResponse user = usersService.getUserById(id);
        return Objects.nonNull(user) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(String id, UserRequest userRequest) {

        UserResponse updatedUser = usersService.updateUser(id, userRequest);
        ResponseEntity<UserResponse> response = ResponseEntity.notFound().build();

        if(Objects.nonNull(updatedUser)){
            if(Objects.nonNull(updatedUser.getId()) && updatedUser.getId().equals(id)){
                response = ResponseEntity.ok(updatedUser);
            }else{
                response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return response;
    }
}
