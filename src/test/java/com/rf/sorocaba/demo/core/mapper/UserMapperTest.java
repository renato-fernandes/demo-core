package com.rf.sorocaba.demo.core.mapper;

import com.rf.sorocaba.demo.core.UserTest;
import com.rf.sorocaba.demo.core.entity.Users;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserMapperTest extends UserTest {

    @Test
    public void toEntityTest(){

        UserRequest userRequest = getSampleUserRequest();

        Users result =  UserMapper.toEntity(userRequest);

        assertNull(result.getId());
        assertEquals(userRequest.getUsername(), result.getUsername());
        assertEquals(userRequest.getEmail(), result.getEmail());
        assertEquals(userRequest.getName(), result.getName());
        assertEquals(userRequest.getLastName(), result.getLastName());
        assertEquals(userRequest.getPassword(), result.getPassword());
        assertEquals(UserStatus.ACTIVE.equals(userRequest.getStatus()), result.getStatus());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    public void toEntityNullableTest(){

        UserRequest userRequest = getUserRequest(
                null,
                TEST_USERNAME_2,
                null,
                TEST_EMAIL_2,
                TEST_PASSWORD_2,
                TEST_STATUS_2);

        Users result =  UserMapper.toEntity(userRequest);

        assertNull(result.getId());
        assertEquals(userRequest.getUsername(), result.getUsername());
        assertEquals(userRequest.getEmail(), result.getEmail());
        assertNull(result.getName());
        assertNull(result.getLastName());
        assertEquals(userRequest.getPassword(), result.getPassword());
        assertEquals(UserStatus.ACTIVE.equals(userRequest.getStatus()), result.getStatus());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    public void toResponseTest(){

        Users users = getUsers(1L);
        users.setUpdatedAt(OffsetDateTime.now());

        UserResponse result = UserMapper.toResponse(users);

        assertEquals(users.getId(), Long.valueOf(result.getId()));
        assertEquals(users.getUsername(), result.getUsername());
        assertEquals(users.getEmail(), result.getEmail());
        assertEquals(users.getName(), result.getName());
        assertEquals(users.getLastName(), result.getLastName());
        assertEquals(users.getPassword(), result.getPassword());
        assertEquals(users.getStatus(), UserStatus.ACTIVE.equals(result.getStatus()));
        assertEquals(users.getCreatedAt(), result.getCreatedAt());
        assertEquals(users.getUpdatedAt(), result.getUpdatedAt());

    }

    @Test
    public void toResponseNullableTest(){

        Users users = new Users();

        UserResponse result = UserMapper.toResponse(users);

        assertNull(result.getId());
        assertNull(result.getUsername());
        assertNull(result.getEmail());
        assertNull(result.getName());
        assertNull(result.getLastName());
        assertNull(result.getPassword());
        assertNull(result.getStatus());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());

    }

    @Test
    public void toResponseInactiveTest(){

        Users users = new Users();
        users.setStatus(false);

        UserResponse result = UserMapper.toResponse(users);

        assertEquals(users.getStatus(), UserStatus.ACTIVE.equals(result.getStatus()));

    }

}
