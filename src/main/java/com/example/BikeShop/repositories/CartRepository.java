package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Cart;
import com.example.BikeShop.models.Client;
import com.example.BikeShop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByClientAndProduct(Client client, Product product);

    Iterable<Cart> findByClientUserUsername(String username);

    Iterable<Cart> findByClientUserUsernameAndProductActive(String username, boolean active);
}