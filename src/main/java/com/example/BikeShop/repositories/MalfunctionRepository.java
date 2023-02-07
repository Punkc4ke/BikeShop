package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Malfunction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MalfunctionRepository extends JpaRepository<Malfunction, Long> {

    Malfunction findByName(String name);
}
