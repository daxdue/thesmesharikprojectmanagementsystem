package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.Statuses;
import com.smeshariks.pms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Project findByTitle(String title);

    @Modifying
    @Query("SELECT p FROM Project p WHERE p.currentStatus = :status")
    List<Project> findByCurrentStatus(@Param(value = "status") Integer status);

    @Modifying
    @Query("update Project p set p.description = :description, p.cost = :cost, p.startTime = :startTime, p.deadTime = :deadTime, p.currentStatus = :currentStatus, p.deliveryAddress = :deliveryAddress where p.id = :id")
    void edit(@Param(value = "description") String description, @Param(value = "cost") Double cost,
              @Param(value = "startTime") Timestamp startTime, @Param(value = "deadTime") Timestamp deadTime,
              @Param(value = "currentStatus") Integer currentStatus, @Param(value = "deliveryAddress") String deliveryAddress,
              @Param(value = "id") Integer id);

    List<Project> findAllByOwner(User user);
    List<Project> findAllByOwnerAndCurrentStatus(User user, Integer status);
}
