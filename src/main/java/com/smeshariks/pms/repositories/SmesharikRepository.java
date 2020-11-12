package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Smesharik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmesharikRepository extends JpaRepository<Smesharik, Integer> {
}
