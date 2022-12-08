package com.example.attestationspring.repositories;

import com.example.attestationspring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Person, Integer>
{

}
