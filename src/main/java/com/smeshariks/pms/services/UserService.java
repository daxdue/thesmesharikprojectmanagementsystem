package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Role;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.repositories.RoleRepository;
import com.smeshariks.pms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Integer userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb != null) {
            return false;
        }

        switch (user.getUserRole()) {
            case ADMIN:
                user.setRoles(Collections.singleton(new Role(1L, "ROLE_ADMIN")));
                break;

            case CUSTOMER:
                user.setRoles(Collections.singleton(new Role(2L, "ROLE_CUSTOMER")));
                break;

            case WORKER:
                user.setRoles(Collections.singleton(new Role(3L, "ROLE_WORKER")));
                break;

            case MANAGER:
                user.setRoles(Collections.singleton(new Role(4L, "ROLE_CUSTOMER")));
                break;

            case WAREHOUSEMAN:
                user.setRoles(Collections.singleton(new Role(5L, "ROLE_WAREHOUSEMAN")));
                break;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    public boolean deleteUser(Integer userId) {
        if(userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }


}
