package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Role;
import com.smeshariks.pms.entities.SmesharikCredentials;
import com.smeshariks.pms.repositories.RoleRepository;
import com.smeshariks.pms.repositories.SmesharikCredentialsRepository;
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
public class SmesharikCredentialsService implements UserDetailsService {

    @Autowired
    SmesharikCredentialsRepository smesharikCredentialsRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SmesharikCredentials smesharikCredentials = smesharikCredentialsRepository.findByUsername(username);

        if(smesharikCredentials == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return smesharikCredentials;
    }

    public SmesharikCredentials findCredentialsById(Long credentialsId) {
        Optional<SmesharikCredentials> smesharikCredentialsDb = smesharikCredentialsRepository.findById(credentialsId);
        return smesharikCredentialsDb.orElse(new SmesharikCredentials());
    }

    public List<SmesharikCredentials> allCredentials() {
        return smesharikCredentialsRepository.findAll();
    }

    public boolean saveCredentials(SmesharikCredentials smesharikCredentials) {
        SmesharikCredentials smesharikCredentialsDb = smesharikCredentialsRepository.findByUsername(smesharikCredentials.getUsername());

        if(smesharikCredentialsDb != null) {
            return false;
        }
        
        smesharikCredentials.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        smesharikCredentials.setPassword(bCryptPasswordEncoder.encode(smesharikCredentials.getPassword()));
        smesharikCredentialsRepository.save(smesharikCredentials);
        return true;
    }
}
