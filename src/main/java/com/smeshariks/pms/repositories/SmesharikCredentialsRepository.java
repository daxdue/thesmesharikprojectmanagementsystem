package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.SmesharikCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmesharikCredentialsRepository extends JpaRepository<SmesharikCredentials, Long> {

    SmesharikCredentials findByUsername(String userame);
}
