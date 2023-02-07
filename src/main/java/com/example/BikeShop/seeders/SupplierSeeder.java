package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Supplier;
import com.example.BikeShop.repositories.SupplierRepository;

import java.util.ArrayList;
import java.util.List;

public class SupplierSeeder {

    private static final List<Supplier> supplierList = new ArrayList<>();

    private static void init() {
        supplierList.add(new Supplier("ВИВО", "+7(999)991-91-91"));
        supplierList.add(new Supplier("Комета", "+7(999)992-92-92"));
        supplierList.add(new Supplier("ИПСтас", "+7(999)993-93-93"));
        supplierList.add(new Supplier("Дельфин", "+7(999)994-94-94"));
        supplierList.add(new Supplier("ШаловливыйЯзычек", "+7(999)995-95-95"));
    }

    public static void seed(SupplierRepository supplierRepository) {
        init();
        for (Supplier supplier : supplierList)
            if (supplierRepository.findByPhoneNumber(supplier.getPhoneNumber()) == null)
                supplierRepository.save(supplier);
    }
}