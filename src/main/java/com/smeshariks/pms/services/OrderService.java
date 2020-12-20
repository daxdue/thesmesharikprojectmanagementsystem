package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Order;
import com.smeshariks.pms.entities.RequestStatus;

import java.util.List;

public interface OrderService {

    Order findOrderById(Integer id);
    List<Order> findAllOrders();
    List<Order> findOrdersByStatus(RequestStatus status);
    void save(Order order);
}
