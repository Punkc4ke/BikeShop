package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Booking;
import com.example.BikeShop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Iterable<Booking> findByProductNameContains(String name);

    Booking findByProduct(Product product);
}