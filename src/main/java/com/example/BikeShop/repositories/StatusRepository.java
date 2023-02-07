package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findByName(String name);
}
