package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Order;
import com.smeshariks.pms.entities.RequestStatus;
import com.smeshariks.pms.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrderById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(new Order());
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> findOrdersByStatus(RequestStatus requestStatus) {
        List<Order> orders = new ArrayList<>();
        orders = orderRepository.findAllByStatus(requestStatus.getValue());
        return orders;
    }

    public void save(Order order) {
        orderRepository.save(order);
    }
}
