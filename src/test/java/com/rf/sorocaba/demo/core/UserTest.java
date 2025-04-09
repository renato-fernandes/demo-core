package com.rf.sorocaba.demo.core;

import com.rf.sorocaba.demo.core.entity.Users;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.model.UserStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class UserTest {

    protected static final String TEST_NAME = "Name";
    protected static final String TEST_USERNAME = "username_test";
    protected static final String TEST_LASTNAME = "Test";
    protected static final String TEST_EMAIL = "test@email.com";
    protected static final String TEST_PASSWORD = "123";
    protected static final String TEST_STATUS = "ACTIVE";

    protected static final String TEST_NAME_2 = "Another";
    protected static final String TEST_USERNAME_2 = "another_username_test";
    protected static final String TEST_LASTNAME_2 = "TestName";
    protected static final String TEST_EMAIL_2 = "another.test@email.com";
    protected static final String TEST_PASSWORD_2 = "456";
    protected static final String TEST_STATUS_2 = "INACTIVE";

    protected UserRequest getSampleUserRequest(){
        return getUserRequest(TEST_NAME, TEST_USERNAME, TEST_LASTNAME,
                TEST_EMAIL, TEST_PASSWORD, TEST_STATUS);
    }

    protected UserRequest getUserRequest(String name, String username, String lastName,
                                       String email, String password, String status){
        return new UserRequest()
                .name(name)
                .username(username)
                .lastName(lastName)
                .email(email)
                .password(password)
                .status(UserStatus.valueOf(status));
    }

    protected Users getUsers(Long id){
        Users users = new Users();
        users.setId(id);
        users.setUsername(TEST_USERNAME);
        users.setEmail(TEST_EMAIL);
        users.setName(TEST_NAME);
        users.setLastName(TEST_LASTNAME);
        users.setPassword(TEST_PASSWORD);
        users.setStatus(true);
        users.setCreatedAt(Instant.now());
        return users;
    }

    protected UserResponse getUserResponse(Long id){

        UserResponse response = new UserResponse();

        response.setId(id.toString());
        response.setUsername(TEST_USERNAME);
        response.setName(TEST_NAME);
        response.setLastName(TEST_LASTNAME);
        response.setEmail(TEST_EMAIL);
        response.setPassword(TEST_PASSWORD);

        return response;
    }

    protected List<Users> getUsersList(){
        List<Users> usersList = new ArrayList<>();
        Users users = new Users();
        users.setId(2L);
        users.setUsername(TEST_USERNAME_2);
        users.setEmail(TEST_EMAIL_2);
        users.setName(TEST_NAME_2);
        users.setLastName(TEST_LASTNAME_2);
        users.setPassword(TEST_PASSWORD_2);
        users.setStatus(false);
        users.setCreatedAt(Instant.now());
        users.setUpdatedAt(Instant.now());

        usersList.add(getUsers(1L));
        usersList.add(users);

        return usersList;
    }

}
