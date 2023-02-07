package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Booking;
import com.example.BikeShop.models.Product;
import com.example.BikeShop.repositories.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingProductSeeder {

    private static final List<Booking> bookingList = new ArrayList<>();

    private static final List<Product> productList = new ArrayList<>();

    private static void init(StatusRepository statusRepository, CategoryRepository categoryRepository, ColorRepository colorRepository,
                             ClientRepository clientRepository, EmployeeRepository employeeRepository, StorageRepository storageRepository,
                             SupplierRepository supplierRepository) {
        productList.add(new Product("Велосипед", 9999, new Date(), 100, 12, true, null, categoryRepository.findByName("Спортивный"), colorRepository.findByName("Черный"), storageRepository.findByAddress("046653, Тюменская область, город Ступино, бульвар Балканская, 67"), supplierRepository.findByPhoneNumber("+7(999)991-91-91")));
        productList.add(new Product("Рама", 6499, new Date(), 300, 1, true, null, categoryRepository.findByName("Горный"), colorRepository.findByName("Красный"), storageRepository.findByAddress("879450, Ульяновская область, город Сергиев Посад, шоссе Космонавтов, 90"), supplierRepository.findByPhoneNumber("+7(999)992-92-92")));
        productList.add(new Product("Колеса", 2999, new Date(), 200, 3, true, null, categoryRepository.findByName("Детский"), colorRepository.findByName("Белый"), storageRepository.findByAddress("086597, Тамбовская область, город Домодедово, бульвар Балканская, 13"), supplierRepository.findByPhoneNumber("+7(999)993-93-93")));
        productList.add(new Product("Гудок", 1999, new Date(), 50, 6, true, null, categoryRepository.findByName("Стандарт"), colorRepository.findByName("Черный"), storageRepository.findByAddress("550196, Читинская область, город Ступино, пр. Ленина, 35"), supplierRepository.findByPhoneNumber("+7(999)994-94-94")));
        productList.add(new Product("Спицы", 1999, new Date(), 2500, 2, true, null, categoryRepository.findByName("Электро"), colorRepository.findByName("Желтый"), storageRepository.findByAddress("070007, Волгоградская область, город Павловский Посад, спуск Сталина, 09"), supplierRepository.findByPhoneNumber("+7(999)995-95-95")));

        bookingList.add(new Booking(new Date(), null, productList.get(0), statusRepository.findByName("Ожидает подтверждения"), clientRepository.findByUserUsername("CLIENT1"), employeeRepository.findByUserUsername("REPAIR_DEP")));
        bookingList.add(new Booking(new Date(), null, productList.get(1), statusRepository.findByName("Подтвержден"), clientRepository.findByUserUsername("CLIENT2"), employeeRepository.findByUserUsername("REPAIR_DEP")));
        bookingList.add(new Booking(new Date(), null, productList.get(2), statusRepository.findByName("В процессе"), clientRepository.findByUserUsername("CLIENT3"), employeeRepository.findByUserUsername("REPAIR_DEP")));
        bookingList.add(new Booking(new Date(), null, productList.get(3), statusRepository.findByName("Готов к выдаче"), clientRepository.findByUserUsername("CLIENT4"), employeeRepository.findByUserUsername("REPAIR_DEP")));
        bookingList.add(new Booking(new Date(), null, productList.get(4), statusRepository.findByName("Ошибка подтверждения"), clientRepository.findByUserUsername("CLIENT5"), employeeRepository.findByUserUsername("REPAIR_DEP")));
    }

    public static void seed(BookingRepository bookingRepository, ProductRepository productRepository, StatusRepository statusRepository,
                            CategoryRepository categoryRepository, ColorRepository colorRepository, ClientRepository clientRepository,
                            EmployeeRepository employeeRepository, StorageRepository storageRepository, SupplierRepository supplierRepository) {
        init(statusRepository, categoryRepository, colorRepository, clientRepository, employeeRepository, storageRepository, supplierRepository);
        for (int i = 0; i < productList.size(); i++)
            if (productRepository.findByName(productList.get(i).getName()) == null) {
                productRepository.save(productList.get(i));
                bookingRepository.save(bookingList.get(i));
            }
    }
}
