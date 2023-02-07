package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {

    Color findByName(String name);
}