package com.rf.sorocaba.demo.core;

import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserStatus;

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

}
