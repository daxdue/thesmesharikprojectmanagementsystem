package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Role;
import com.smeshariks.pms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    List<User> findUsersByRole(Role role);
    List<User> findAllByRoleName(String name);
}
