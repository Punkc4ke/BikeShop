package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Image;
import com.example.BikeShop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findImageByProduct(Product product);
}
