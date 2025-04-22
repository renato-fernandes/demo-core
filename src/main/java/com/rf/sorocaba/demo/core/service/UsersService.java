package com.rf.sorocaba.demo.core.service;

import com.rf.sorocaba.demo.core.entity.Users;
import com.rf.sorocaba.demo.core.mapper.UserMapper;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsersService {

    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        UserResponse user = null;
        if(Objects.nonNull(userRequest) && otherEqualUserDoesNotExists(userRequest)){

            Users users = UserMapper.toEntity(userRequest);
            users.setCreatedAt(OffsetDateTime.now());

            Users newUsers = null;
            try {
                newUsers = usersRepository.save(users);
            } catch (DataIntegrityViolationException e) {
                log.error("[UsersService] DataIntegrityViolationException - " +
                        "invalid User object: verify if object UserRequest has all non-null parameters and if it is valid");
                return null;
            }
            log.info("[UsersService] New User created - id: {}, username:{}", newUsers.getId(), newUsers.getUsername());
            user = UserMapper.toResponse(newUsers);
        }
        return user;
    }

    public UserResponse deleteUser(String id) {

        log.debug("[UsersService] Request to delete user - id: {}", id);

        UserResponse userToBeDeleted = getUserById(id);
        usersRepository.deleteById(Long.valueOf(id));
        return Objects.nonNull(userToBeDeleted) ? userToBeDeleted : null;
    }

    public List<UserResponse> getAllUsers() {
        List<Users> usersList = usersRepository.findAll();

        log.debug("[UsersService] List of {} user returned", usersList.size());

        return usersList.stream()
                .map(UserMapper::toResponse)
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
            log.debug("[UsersService] User data requested - id:{}, username:{}", users.getId(), users.getUsername());
        }
        return Objects.nonNull(users) ? UserMapper.toResponse(users) : null;
    }

    public UserResponse updateUser(String id, UserRequest userRequest) {

        Optional<Users> userToBeUpdated = usersRepository.findById(Long.valueOf(id));

        UserResponse updatedUser = null;
        if(userToBeUpdated.isPresent()){
            if(Objects.nonNull(userRequest) && otherEqualUserDoesNotExists(Long.valueOf(id), userRequest)){

                Users users = UserMapper.toEntity(userRequest);
                users.setId(Long.valueOf(id));
                users.setCreatedAt(userToBeUpdated.get().getCreatedAt());
                users.setUpdatedAt(OffsetDateTime.now());

                Users updatedUserEntity = usersRepository.save(users);
                log.info("[UsersService] User updated! - id:{}", id);

                updatedUser = UserMapper.toResponse(updatedUserEntity);
            }else{
                log.warn("[UsersService] Updating User failed! - id:{}", id);
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
