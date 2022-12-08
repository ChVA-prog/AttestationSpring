package com.example.attestationspring.repositories;

import com.example.attestationspring.models.Order;
import com.example.attestationspring.models.Person;
import com.example.attestationspring.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);
    List<Order> findByNumberContainingIgnoreCase(String name);

}
