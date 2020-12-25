package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByStatus(Integer status);
    @Modifying
    @Query("update Order o set o.status = :status where o.id = :id")
    void edit(@Param(value = "status") Integer status, @Param(value = "id") Integer id);
}
