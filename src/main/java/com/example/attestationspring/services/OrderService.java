package com.example.attestationspring.services;

import com.example.attestationspring.models.Order;
import com.example.attestationspring.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService

{
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    public Order getOrderId(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    @Transactional
    public void saveOrder(Order order){
        orderRepository.save(order);
    }
    @Transactional
    public void updateOrder(int id, Order order){
        order.setId(id);
        orderRepository.save(order);
    }
    @Transactional
    public void deleteOrder(int id){
        orderRepository.deleteById(id);
    }
}
