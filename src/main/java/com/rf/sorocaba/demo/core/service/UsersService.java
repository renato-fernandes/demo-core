package com.rf.sorocaba.demo.core.service;

import com.rf.sorocaba.demo.core.entity.Users;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.model.UserStatus;
import com.rf.sorocaba.demo.core.repository.UsersRepository;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsersService {

    //TODO: remove this after implement database integration
    private final Map<String, UserResponse> usersMap = new HashMap<>();

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        UserResponse user = null;
        if(Objects.nonNull(userRequest) && otherEqualUserDoesNotExists(userRequest)){

            Users users = new Users();
            users.setUsername(userRequest.getUsername());
            users.setEmail(userRequest.getEmail());
            users.setName(userRequest.getName());
            users.setLastName(userRequest.getLastName());
            users.setPassword(userRequest.getPassword());
            users.setStatus(UserStatus.ACTIVE.equals(userRequest.getStatus()));
            users.setCreatedAt(Instant.now());

            Users newUsers = null;
            try {
                newUsers = usersRepository.save(users);
            } catch (DataIntegrityViolationException e) {
                System.out.println("DataIntegrityViolationException");
                return null;
            }

            user = new UserResponse()
                    .id(String.valueOf(newUsers.getId()))
                    .username(newUsers.getUsername())
                    .name(newUsers.getName())
                    .lastName(newUsers.getLastName())
                    .email(newUsers.getEmail())
                    .password(newUsers.getPassword())
                    .status(newUsers.getStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE)
                    .createdAt(newUsers.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        return user;
    }

    public UserResponse deleteUser(String id) {
        UserResponse userToBeDeleted = getUserById(id);
        usersRepository.deleteById(Long.valueOf(id));
        return Objects.nonNull(userToBeDeleted) ? userToBeDeleted : null;
    }

    public List<UserResponse> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();

        return usersList.stream()
                .map(users -> new UserResponse()
                        .id(users.getId().toString())
                        .username(users.getUsername())
                        .email(users.getEmail())
                        .name(users.getName())
                        .lastName(users.getLastName())
                        .password(users.getPassword())
                        .status(users.getStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE)
                        .createdAt(users.getCreatedAt().atOffset(ZoneOffset.UTC))
                        .updatedAt(Objects.nonNull(users.getUpdatedAt()) ?
                                users.getUpdatedAt().atOffset(ZoneOffset.UTC) :
                                null))
                .toList();
    }

    public UserResponse getUserById(String id) {
        if(Objects.isNull(id)){
            return null;
        }
        Optional<Users> usersOptional = usersRepository.findById(Long.valueOf(id));
        Users users = null;
        if(usersOptional.isPresent()){
            users =usersOptional.get();
        }
        return Objects.nonNull(users) ? new UserResponse()
                .id(users.getId().toString())
                .username(users.getUsername())
                .email(users.getEmail())
                .name(users.getName())
                .lastName(users.getLastName())
                .password(users.getPassword())
                .status(users.getStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE)
                .createdAt(users.getCreatedAt().atOffset(ZoneOffset.UTC))
                .updatedAt(Objects.nonNull(users.getUpdatedAt()) ?
                        users.getUpdatedAt().atOffset(ZoneOffset.UTC) :
                        null) :
                null;
    }

    public UserResponse updateUser(String id, UserRequest userRequest) {

        Optional<Users> userToBeUpdated = usersRepository.findById(Long.valueOf(id));

        UserResponse updatedUser = null;
        if(userToBeUpdated.isPresent()){
            if(Objects.nonNull(userRequest) && otherEqualUserDoesNotExists(Long.valueOf(id), userRequest)){

                Users users = new Users();
                users.setId(Long.valueOf(id));
                users.setUsername(userRequest.getUsername());
                users.setEmail(userRequest.getEmail());
                users.setName(userRequest.getName());
                users.setLastName(userRequest.getLastName());
                users.setPassword(userRequest.getPassword());
                users.setStatus(UserStatus.ACTIVE.equals(userRequest.getStatus()));
                users.setCreatedAt(userToBeUpdated.get().getCreatedAt());
                users.setUpdatedAt(Instant.now());

                Users updatedUserEntity = usersRepository.save(users);

                updatedUser = new UserResponse()
                        .id(String.valueOf(updatedUserEntity.getId()))
                        .username(updatedUserEntity.getUsername())
                        .name(updatedUserEntity.getName())
                        .lastName(updatedUserEntity.getLastName())
                        .email(updatedUserEntity.getEmail())
                        .password(updatedUserEntity.getPassword())
                        .status(updatedUserEntity.getStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE)
                        .createdAt(updatedUserEntity.getCreatedAt().atOffset(ZoneOffset.UTC))
                        .updatedAt(updatedUserEntity.getCreatedAt().atOffset(ZoneOffset.UTC));
            }else{
                updatedUser = new UserResponse();
            }
        }
        return updatedUser;
    }

    private boolean otherEqualUserDoesNotExists(Long id, UserRequest userRequest) {
        List<Users> usernameList = usersRepository.findByUsername(userRequest.getUsername())
                .stream().filter(u -> !u.getId().equals(id)).toList();
        List<Users> emailList = usersRepository.findByEmail(userRequest.getEmail())
                .stream().filter(u -> !u.getId().equals(id)).toList();
        return usernameList.isEmpty() && emailList.isEmpty();
    }

    private boolean otherEqualUserDoesNotExists(UserRequest userRequest) {
        List<Users> usernameList = usersRepository.findByUsername(userRequest.getUsername());
        List<Users> emailList = usersRepository.findByEmail(userRequest.getEmail());
        return usernameList.isEmpty() && emailList.isEmpty();
    }
}
