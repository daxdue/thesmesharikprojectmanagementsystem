package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Timings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimingsRepository extends JpaRepository<Timings, Integer   > {
}
