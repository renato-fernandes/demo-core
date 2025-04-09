package com.rf.sorocaba.demo.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rf.sorocaba.demo.core.UserTest;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.model.UserStatus;
import com.rf.sorocaba.demo.core.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UsersControllerTest extends UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsersService usersService;

    private static final String ID = "123";

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    public void setup(){
        userRequest = getSampleUserRequest();
        userResponse = new UserResponse()
                .id(ID)
                .name(TEST_NAME)
                .username(TEST_USERNAME)
                .lastName(TEST_LASTNAME)
                .email(TEST_EMAIL)
                .status(UserStatus.valueOf(TEST_STATUS))
                .password(TEST_PASSWORD)
                .createdAt(OffsetDateTime.now());
    }

    @Test
    public void createUserTest() throws Exception {

        when(usersService.createUser(any())).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(usersService, times(1)).createUser(userRequest);
    }

    @Test
    public void createNullUserTest() throws Exception {

        when(usersService.createUser(any())).thenReturn(null);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict());

        verify(usersService, times(1)).createUser(userRequest);
    }

    @Test
    public void deleteUserTest() throws Exception {

        when(usersService.deleteUser(any())).thenReturn(userResponse);

        mockMvc.perform(delete("/users/{id}", userResponse.getId()))
                .andExpect(status().isOk());

        verify(usersService, times(1)).deleteUser(userResponse.getId());
    }

    @Test
    public void deleteNonExistingUserTest() throws Exception {

        when(usersService.deleteUser(any())).thenReturn(null);

        mockMvc.perform(delete("/users/{id}", ID))
                .andExpect(status().isNotFound());

        verify(usersService, times(1)).deleteUser(ID);
    }

    @Test
    public void getAllUsersTest() throws Exception {

        List<UserResponse> result = Collections.singletonList(userResponse);
        when(usersService.getAllUsers()).thenReturn(result);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));

        verify(usersService, times(1)).getAllUsers();
    }

    @Test
    public void getAllUsersNullTest() throws Exception {

        when(usersService.getAllUsers()).thenReturn(null);

        mockMvc.perform(get("/users"))
                .andExpect(status().isNotFound());

        verify(usersService, times(1)).getAllUsers();
    }

    @Test
    public void getUserByIdTest() throws Exception {

        when(usersService.getUserById(any())).thenReturn(userResponse);

        mockMvc.perform(get("/users/{id}", userResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(usersService, times(1)).getUserById(userResponse.getId());
    }

    @Test
    public void getNullUserByIdTest() throws Exception {

        when(usersService.getUserById(any())).thenReturn(null);

        mockMvc.perform(get("/users/{id}", ID))
                .andExpect(status().isNotFound());

        verify(usersService, times(1)).getUserById(userResponse.getId());
    }

    @Test
    public void updateUserTest() throws Exception {

        when(usersService.updateUser(any(), any())).thenReturn(userResponse);

        mockMvc.perform(put("/users/{id}", userResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(usersService, times(1)).updateUser(userResponse.getId(), userRequest);
    }

    @Test
    public void updateUserConflictingWithOtherUserTest() throws Exception {

        when(usersService.updateUser(any(), any())).thenReturn(userResponse);

        mockMvc.perform(put("/users/{id}", "321")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isConflict());

        verify(usersService, times(1)).updateUser("321", userRequest);
    }

    @Test
    public void updateUserWithNullIdTest() throws Exception {

        when(usersService.updateUser(any(), any())).thenReturn(new UserResponse());

        mockMvc.perform(put("/users/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isConflict());

        verify(usersService, times(1)).updateUser(ID, userRequest);
    }

    @Test
    public void updateNonExistingUserTest() throws Exception {

        when(usersService.updateUser(any(), any())).thenReturn(null);

        mockMvc.perform(put("/users/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isNotFound());

        verify(usersService, times(1)).updateUser(ID, userRequest);
    }

}
