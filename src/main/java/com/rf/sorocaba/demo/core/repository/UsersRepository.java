package com.rf.sorocaba.demo.core.repository;

import com.rf.sorocaba.demo.core.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    List<Users> findByUsername(String username);
    List<Users> findByEmail(String email);

}
