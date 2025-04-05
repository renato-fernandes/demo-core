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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    public void setup(){
        userRequest = getSampleUserRequest();
        userResponse = new UserResponse()
                .id(UUID.randomUUID().toString())
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

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        response.andExpect(status().isOk());
        response.andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    public void createNullUserTest() throws Exception {

        when(usersService.createUser(any())).thenReturn(null);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        response.andExpect(status().isConflict());
    }

    @Test
    public void deleteUserTest() throws Exception {

        when(usersService.deleteUser(any())).thenReturn(userResponse);

        ResultActions response = mockMvc.perform(delete("/users/{id}", userResponse.getId()));

        response.andExpect(status().isOk());
    }

    @Test
    public void deleteNonExistingUserTest() throws Exception {

        when(usersService.deleteUser(any())).thenReturn(null);

        ResultActions response = mockMvc.perform(delete("/users/{id}", "123"));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getAllUsersTest() throws Exception {

        List<UserResponse> result = Collections.singletonList(userResponse);
        when(usersService.getAllUsers()).thenReturn(result);

        ResultActions response = mockMvc.perform(get("/users"));

        response.andExpect(status().isOk());
        response.andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    public void getAllUsersNullTest() throws Exception {

        when(usersService.getAllUsers()).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/users"));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getUserByIdTest() throws Exception {

        when(usersService.getUserById(any())).thenReturn(userResponse);

        ResultActions response = mockMvc.perform(get("/users/{id}", userResponse.getId()));

        response.andExpect(status().isOk());
    }

    @Test
    public void getNullUserByIdTest() throws Exception {

        when(usersService.getUserById(any())).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/users/{id}", "123"));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateUserTest() throws Exception {

        when(usersService.updateUser(any(), any())).thenReturn(userResponse);

        ResultActions response = mockMvc.perform(put("/users/{id}", userResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        response.andExpect(status().isOk());
        response.andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    public void updateUserConflictingWithOtherUserTest() throws Exception {

        when(usersService.updateUser(any(), any())).thenReturn(userResponse);

        ResultActions response = mockMvc.perform(put("/users/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        response.andExpect(status().isConflict());
    }

    @Test
    public void updateUserWithNullIdTest() throws Exception {


        when(usersService.updateUser(any(), any())).thenReturn(new UserResponse());

        ResultActions response = mockMvc.perform(put("/users/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        response.andExpect(status().isConflict());
    }

    @Test
    public void updateNonExistingUserTest() throws Exception {

        when(usersService.updateUser(any(), any())).thenReturn(null);

        ResultActions response = mockMvc.perform(put("/users/{id}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        response.andExpect(status().isNotFound());
    }

}
