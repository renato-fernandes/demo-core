package com.rf.sorocaba.demo.core.service;

import com.rf.sorocaba.demo.core.UserTest;
import com.rf.sorocaba.demo.core.entity.Users;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.model.UserStatus;
import com.rf.sorocaba.demo.core.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsersServiceTest extends UserTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;

    private UserRequest sampleUserRequest;
    private final long id = 1L;
    private Users users;

    @BeforeEach
    public void setup(){
        sampleUserRequest = getSampleUserRequest();
        users = getUsers(id);
    }

    @Test
    public void createUserTest(){

        when(usersRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.save(any())).thenReturn(users);

        UserResponse result = usersService.createUser(sampleUserRequest);

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
    public void createInactiveUserTest(){

        Users users = getUsers(id);
        users.setStatus(false);

        when(usersRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.save(any())).thenReturn(users);

        UserResponse result = usersService.createUser(sampleUserRequest);

        assertNotNull(result);
        assertEquals(UserStatus.INACTIVE, result.getStatus());
    }

    @Test
    public void createEmptyUserTest(){

        when(usersRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        UserResponse result = usersService.createUser(new UserRequest());

        assertNull(result);
    }

    @Test
    public void createNullUserTest(){

        UserResponse result = usersService.createUser(null);

        assertNull(result);
    }

    @Test
    public void createExistingUserTest(){

        when(usersRepository.findByUsername(anyString())).thenReturn(Collections.singletonList(users));
        when(usersRepository.findByEmail(anyString())).thenReturn(Collections.singletonList(users));

        UserResponse result = usersService.createUser(sampleUserRequest);

        assertNull(result);
    }

    @Test
    public void createExistingUserNameTest(){

        when(usersRepository.findByUsername(anyString())).thenReturn(Collections.singletonList(users));
        when(usersRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());

        UserResponse result = usersService.createUser(sampleUserRequest);

        assertNull(result);
    }

    @Test
    public void createExistingUserEmailTest(){

        when(usersRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.findByEmail(anyString())).thenReturn(Collections.singletonList(users));

        UserResponse result = usersService.createUser(sampleUserRequest);

        assertNull(result);
    }

    @Test
    public void deleteUserTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));
        doNothing().when(usersRepository).deleteById(anyLong());

        UserResponse result = usersService.deleteUser(users.getId().toString());

        assertNotNull(result);
        assertEquals(users.getId().toString(), result.getId());
    }

    @Test
    public void deleteNonExistingUserTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(usersRepository).deleteById(anyLong());

        UserResponse result = usersService.deleteUser(String.valueOf(id));

        assertNull(result);
    }

    @Test
    public void getAllUsersTest(){

        when(usersRepository.findAll()).thenReturn(getUsersList());

        List<UserResponse> resultList = usersService.getAllUsers();

        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        assertEquals(2, resultList.size());
    }

    @Test
    public void getEmptyAllUsersTest(){

        when(usersRepository.findAll()).thenReturn(new ArrayList<>());

        List<UserResponse> resultList = usersService.getAllUsers();

        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void getUserByIdTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));

        UserResponse result = usersService.getUserById(String.valueOf(id));

        assertNotNull(result);
        assertEquals(String.valueOf(id), result.getId());
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(TEST_EMAIL, result.getEmail());
        assertEquals(TEST_NAME, result.getName());
        assertEquals(TEST_LASTNAME, result.getLastName());
        assertEquals(TEST_PASSWORD, result.getPassword());
        assertEquals(TEST_STATUS, result.getStatus().getValue());
        assertEquals(TEST_STATUS, result.getStatus().getValue());
        assertEquals(users.getCreatedAt().atOffset(ZoneOffset.UTC), result.getCreatedAt());
    }

    @Test
    public void getInactiveUserByIdTest(){

        Users users = getUsers(id);
        users.setStatus(false);
        users.setUpdatedAt(Instant.now());

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));

        UserResponse result = usersService.getUserById(String.valueOf(id));

        assertNotNull(result);
        assertEquals(String.valueOf(id), result.getId());
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(TEST_EMAIL, result.getEmail());
        assertEquals(TEST_NAME, result.getName());
        assertEquals(TEST_LASTNAME, result.getLastName());
        assertEquals(TEST_PASSWORD, result.getPassword());
        assertEquals(TEST_STATUS_2, result.getStatus().getValue());
        assertEquals(users.getCreatedAt().atOffset(ZoneOffset.UTC), result.getCreatedAt());
    }

    @Test
    public void getUserByNonExistingIdTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserResponse result = usersService.getUserById(String.valueOf(id));

        assertNull(result);
    }

    @Test
    public void getUserByNullIdTest(){

        UserResponse result = usersService.getUserById(null);

        assertNull(result);
    }

    @Test
    public void updateUserTest(){

        Users updatedUser = new Users();
        updatedUser.setId(users.getId());
        updatedUser.setUsername(TEST_USERNAME_2);
        updatedUser.setEmail(TEST_EMAIL_2);
        updatedUser.setName(TEST_NAME_2);
        updatedUser.setLastName(TEST_LASTNAME_2);
        updatedUser.setPassword(TEST_PASSWORD_2);
        updatedUser.setStatus(false);
        updatedUser.setCreatedAt(users.getCreatedAt());
        updatedUser.setUpdatedAt(Instant.now());

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));
        when(usersRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.save(any())).thenReturn(updatedUser);

        UserResponse result =  usersService.updateUser(users.getId().toString(), sampleUserRequest);

        assertNotNull(result);
        assertEquals(users.getId().toString(), result.getId());
        assertEquals(TEST_NAME_2, result.getName());
        assertEquals(TEST_USERNAME_2, result.getUsername());
        assertEquals(TEST_LASTNAME_2, result.getLastName());
        assertEquals(TEST_EMAIL_2, result.getEmail());
        assertEquals(TEST_PASSWORD_2, result.getPassword());
        assertEquals(TEST_STATUS_2, result.getStatus().getValue());
        assertEquals(users.getCreatedAt().atOffset(ZoneOffset.UTC), result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    public void updateActiveUserTest(){

        Users updatedUser = new Users();
        updatedUser.setId(users.getId());
        updatedUser.setUsername(TEST_USERNAME_2);
        updatedUser.setEmail(TEST_EMAIL_2);
        updatedUser.setName(TEST_NAME_2);
        updatedUser.setLastName(TEST_LASTNAME_2);
        updatedUser.setPassword(TEST_PASSWORD_2);
        updatedUser.setStatus(true);
        updatedUser.setCreatedAt(users.getCreatedAt());
        updatedUser.setUpdatedAt(Instant.now());

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));
        when(usersRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.save(any())).thenReturn(updatedUser);

        UserResponse result =  usersService.updateUser(users.getId().toString(), sampleUserRequest);

        assertNotNull(result);
        assertEquals(TEST_STATUS, result.getStatus().getValue());
    }

    @Test
    public void updateNonExistingUserTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserResponse result =  usersService.updateUser(users.getId().toString() ,sampleUserRequest);

        assertNull(result);
    }

    @Test
    public void updateExistingUserNameTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));
        when(usersRepository.findByUsername(anyString())).thenReturn(Collections.singletonList(getUsers(123L)));
        when(usersRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());

        UserResponse result =  usersService.updateUser(users.getId().toString() ,sampleUserRequest);

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    public void updateExistingUserEmailTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));
        when(usersRepository.findByUsername(anyString())).thenReturn(new ArrayList<>());
        when(usersRepository.findByEmail(anyString())).thenReturn(Collections.singletonList(getUsers(123L)));

        UserResponse result =  usersService.updateUser(users.getId().toString() ,sampleUserRequest);

        assertNotNull(result);
        assertNull(result.getId());
    }

    @Test
    public void updateUserNullTest(){

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(users));

        UserResponse result = usersService.updateUser(users.getId().toString(), null);

        assertNotNull(result);
        assertNull(result.getId());
    }
}
