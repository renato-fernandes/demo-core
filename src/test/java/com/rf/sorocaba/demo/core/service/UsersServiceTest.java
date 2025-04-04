package com.rf.sorocaba.demo.core.service;

import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UsersServiceTest {

    private static final String TEST_NAME = "Name";
    private static final String TEST_USERNAME = "username_test";
    private static final String TEST_LASTNAME = "Test";
    private static final String TEST_EMAIL = "test@email.com";
    private static final String TEST_PASSWORD = "123";
    private static final String TEST_STATUS = "ACTIVE";

    private static final String TEST_NAME_2 = "Another";
    private static final String TEST_USERNAME_2 = "another_username_test";
    private static final String TEST_LASTNAME_2 = "TestName";
    private static final String TEST_EMAIL_2 = "another.test@email.com";
    private static final String TEST_PASSWORD_2 = "456";
    private static final String TEST_STATUS_2 = "INACTIVE";

    private final UserRequest sampleUserRequest = getSampleUserRequest();

    @Test
    public void createUserTest(){

        UserResponse result = getUsersService().createUser(sampleUserRequest);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(TEST_NAME, result.getName());
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(TEST_LASTNAME, result.getLastName());
        assertEquals(TEST_EMAIL, result.getEmail());
        assertEquals(TEST_PASSWORD, result.getPassword());
        assertEquals(TEST_STATUS, result.getStatus().getValue());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    public void createEmptyUserTest(){

        UserResponse result = getUsersService().createUser(new UserRequest());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNull(result.getName());
        assertNull(result.getUsername());
        assertNull(result.getLastName());
        assertNull(result.getEmail());
        assertNull(result.getPassword());
        assertNull(result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    public void createNullUserTest(){

        UserResponse result = getUsersService().createUser(null);

        assertNull(result);
    }

    @Test
    public void createExistingUserTest(){

        UsersService usersService = getUsersService();

        usersService.createUser(sampleUserRequest);
        UserResponse result = usersService.createUser(sampleUserRequest);

        assertNull(result);
    }

    @Test
    public void createExistingUserNameTest(){

        UsersService usersService = getUsersService();

        usersService.createUser(sampleUserRequest);
        UserResponse result = usersService.createUser(new UserRequest()
                .username(TEST_USERNAME)
                .email("new@email.com"));

        assertNull(result);
    }

    @Test
    public void createExistingUserEmailTest(){

        UsersService usersService = getUsersService();

        usersService.createUser(sampleUserRequest);
        UserResponse result = usersService.createUser(new UserRequest()
                .username("New")
                .email(TEST_EMAIL));

        assertNull(result);
    }

    @Test
    public void deleteUserTest(){

        UsersService usersService = getUsersService();

        UserResponse userToBeDeleted = usersService.createUser(sampleUserRequest);

        UserResponse result = usersService.deleteUser(userToBeDeleted.getId());

        assertNotNull(result);
        assertEquals(userToBeDeleted, result);
    }

    @Test
    public void deleteNonExistingUserTest(){

        UsersService usersService = getUsersService();

        UserResponse result = usersService.deleteUser("test-id");

        assertNull(result);
    }

    @Test
    public void getAllUsersTest(){

        UsersService usersService = getUsersService();

        usersService.createUser(sampleUserRequest);
        usersService.createUser(getUserRequest(TEST_NAME_2, TEST_USERNAME_2, TEST_LASTNAME_2,
                TEST_EMAIL_2, TEST_PASSWORD_2, TEST_STATUS_2));

        List<UserResponse> resultList = usersService.getAllUsers();

        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());
    }

    @Test
    public void getAllUniqueUsersTest(){

        UsersService usersService = getUsersService();

        usersService.createUser(sampleUserRequest);
        usersService.createUser(sampleUserRequest);

        List<UserResponse> resultList = usersService.getAllUsers();

        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
    }

    @Test
    public void getEmptyAllUsersTest(){

        UsersService usersService = getUsersService();

        List<UserResponse> resultList = usersService.getAllUsers();

        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void getUserByIdTest(){

        UsersService usersService = getUsersService();

        UserResponse userResponse = usersService.createUser(sampleUserRequest);
        usersService.createUser(getUserRequest(TEST_NAME_2, TEST_USERNAME_2, TEST_LASTNAME_2,
                TEST_EMAIL_2, TEST_PASSWORD_2, TEST_STATUS_2));

        UserResponse result = usersService.getUserById(userResponse.getId());

        assertNotNull(result);
        assertEquals(userResponse, result);
    }

    @Test
    public void getUserByNonExistingIdTest(){

        UsersService usersService = getUsersService();

        UserResponse result = usersService.getUserById("test-id");

        assertNull(result);
    }

    @Test
    public void getUserByNullIdTest(){

        UsersService usersService = getUsersService();

        UserResponse result = usersService.getUserById(null);

        assertNull(result);
    }

    @Test
    public void updateUserTest(){

        UsersService usersService = getUsersService();

        UserResponse userToBeUpdated = usersService.createUser(sampleUserRequest);
        UserResponse result =  usersService.updateUser(userToBeUpdated.getId(),
                getUserRequest(TEST_NAME_2, TEST_USERNAME_2, TEST_LASTNAME_2,
                TEST_EMAIL_2, TEST_PASSWORD_2, TEST_STATUS_2));

        assertNotNull(result);
        assertEquals(userToBeUpdated.getId(), result.getId());
        assertEquals(TEST_NAME_2, result.getName());
        assertEquals(TEST_USERNAME_2, result.getUsername());
        assertEquals(TEST_LASTNAME_2, result.getLastName());
        assertEquals(TEST_EMAIL_2, result.getEmail());
        assertEquals(TEST_PASSWORD_2, result.getPassword());
        assertEquals(TEST_STATUS_2, result.getStatus().getValue());
        assertEquals(userToBeUpdated.getCreatedAt(), result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    public void updateNonExistingUserTest(){

        UsersService usersService = getUsersService();

        UserResponse result =  usersService.updateUser("test-id" ,sampleUserRequest);

        assertNull(result);
    }

    @Test
    public void updateExistingUserNameTest(){

        UsersService usersService = getUsersService();

        UserResponse userToBeUpdated = usersService.createUser(sampleUserRequest);
        usersService.createUser(getUserRequest(TEST_NAME_2, TEST_USERNAME_2, TEST_LASTNAME_2,
                        TEST_EMAIL_2, TEST_PASSWORD_2, TEST_STATUS_2));

        UserResponse result = usersService.updateUser(userToBeUpdated.getId(),
                new UserRequest()
                .username(TEST_USERNAME_2)
                .email("new@email.com"));

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    public void updateExistingUserEmailTest(){

        UsersService usersService = getUsersService();

        UserResponse userToBeUpdated = usersService.createUser(sampleUserRequest);
        usersService.createUser(getUserRequest(TEST_NAME_2, TEST_USERNAME_2, TEST_LASTNAME_2,
                TEST_EMAIL_2, TEST_PASSWORD_2, TEST_STATUS_2));

        UserResponse result = usersService.updateUser(userToBeUpdated.getId(),
                new UserRequest()
                .username("New")
                .email(TEST_EMAIL_2));

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    public void updateUserNullTest(){

        UsersService usersService = getUsersService();

        UserResponse userToBeUpdated = usersService.createUser(sampleUserRequest);

        UserResponse result = usersService.updateUser(userToBeUpdated.getId(), null);

        assertNotNull(result);
        assertNull(result.getId());
    }





    private UsersService getUsersService(){
        return new UsersService();
    }

    private UserRequest getSampleUserRequest(){
        return getUserRequest(TEST_NAME, TEST_USERNAME, TEST_LASTNAME,
                TEST_EMAIL, TEST_PASSWORD, TEST_STATUS);
    }

    private UserRequest getUserRequest(String name, String username, String lastName,
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
