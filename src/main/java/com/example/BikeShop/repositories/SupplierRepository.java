package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Iterable<Supplier> findByNameOrganization(String nameOrganization);

    Supplier findByPhoneNumber(String phoneNumber);
}
