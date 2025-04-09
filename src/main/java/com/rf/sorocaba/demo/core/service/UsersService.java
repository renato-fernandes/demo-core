package com.rf.sorocaba.demo.core.service;

import com.rf.sorocaba.demo.core.entity.Users;
import com.rf.sorocaba.demo.core.mapper.UserMapper;
import com.rf.sorocaba.demo.core.model.UserRequest;
import com.rf.sorocaba.demo.core.model.UserResponse;
import com.rf.sorocaba.demo.core.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserResponse createUser(UserRequest userRequest) {
        UserResponse user = null;
        if(Objects.nonNull(userRequest) && otherEqualUserDoesNotExists(userRequest)){

            Users users = UserMapper.toEntity(userRequest);

            Users newUsers = null;
            try {
                newUsers = usersRepository.save(users);
            } catch (DataIntegrityViolationException e) {
                System.out.println("DataIntegrityViolationException");
                return null;
            }

            user = UserMapper.toResponse(newUsers);
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
        }
        return Objects.nonNull(users) ? UserMapper.toResponse(users) : null;
    }

    public UserResponse updateUser(String id, UserRequest userRequest) {

        Optional<Users> userToBeUpdated = usersRepository.findById(Long.valueOf(id));

        UserResponse updatedUser = null;
        if(userToBeUpdated.isPresent()){
            if(Objects.nonNull(userRequest) && otherEqualUserDoesNotExists(Long.valueOf(id), userRequest)){

                Users users = UserMapper.toEntity(userRequest);

                Users updatedUserEntity = usersRepository.save(users);

                updatedUser = UserMapper.toResponse(updatedUserEntity);
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
