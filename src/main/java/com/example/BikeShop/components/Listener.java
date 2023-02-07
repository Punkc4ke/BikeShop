package com.example.BikeShop.components;

import com.example.BikeShop.repositories.*;
import com.example.BikeShop.seeders.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    private final BookingRepository bookingRepository;

    private final CategoryRepository categoryRepository;

    private final ClientRepository clientRepository;

    private final ColorRepository colorRepository;

    private final EmployeeRepository employeeRepository;

    private final MalfunctionRepository malfunctionRepository;

    private final ProductRepository productRepository;

    private final StatusRepository statusRepository;

    private final StorageRepository storageRepository;

    private final SupplierRepository supplierRepository;

    private final UserRepository userRepository;

    public Listener(BookingRepository bookingRepository, CategoryRepository categoryRepository, ClientRepository clientRepository,
                    ColorRepository colorRepository, EmployeeRepository employeeRepository, MalfunctionRepository malfunctionRepository,
                    ProductRepository productRepository, StatusRepository statusRepository, StorageRepository storageRepository,
                    SupplierRepository supplierRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
        this.colorRepository = colorRepository;
        this.employeeRepository = employeeRepository;
        this.malfunctionRepository = malfunctionRepository;
        this.productRepository = productRepository;
        this.statusRepository = statusRepository;
        this.storageRepository = storageRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        UserSeeder.seed(userRepository);
        CategorySeeder.seed(categoryRepository);
        ColorSeeder.seed(colorRepository);
        MalfunctionSeeder.seed(malfunctionRepository);
        StatusSeeder.seed(statusRepository);
        StorageSeeder.seed(storageRepository);
        SupplierSeeder.seed(supplierRepository);
        ClientSeeder.seed(clientRepository, userRepository);
        EmployeeSeeder.seed(employeeRepository, userRepository);
        BookingProductSeeder.seed(bookingRepository, productRepository, statusRepository, categoryRepository, colorRepository,
                clientRepository, employeeRepository, storageRepository, supplierRepository);
    }
}