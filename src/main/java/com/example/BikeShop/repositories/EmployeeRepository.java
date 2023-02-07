package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Iterable<Employee> findByUserUsernameContainsAndUserUsernameNot(String username, String usernameNot);

    Iterable<Employee> findByUserUsernameNot(String username);

    Iterable<Employee> findAllByUserUsernameNot(String username, Sort sort);

    Employee findByUserUsername(String username);
}