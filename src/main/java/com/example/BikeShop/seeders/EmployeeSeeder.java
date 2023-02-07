package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Employee;
import com.example.BikeShop.repositories.EmployeeRepository;
import com.example.BikeShop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeSeeder {

    private static final List<Employee> employeeList = new ArrayList<>();

    private static void init(UserRepository userRepository) {
        employeeList.add(new Employee("Щаников", "Иван", "Максимович", new Date(), "1111", "111111", "Tello 8165 Piso 4 Yáñez del Mar, AR-B 91623", "+7(999)999-99-99", userRepository.findByUsername("ADMIN")));
        employeeList.add(new Employee("Горбунова", "Мария", "Александровна", new Date(), "0987", "654321", "Hoża 63/42, 47-429 Świętochłowice", "+7(999)998-98-98", userRepository.findByUsername("HR_DEP")));
        employeeList.add(new Employee("Пахомов", "Даниил", "Александрович", new Date(), "1234", "567890", "Strada Palumbo 09 Appartamento 72 Jole salentino, 31414 Novara (MT)", "+7(999)997-97-97", userRepository.findByUsername("SALES_DEP")));
        employeeList.add(new Employee("Чурилов", "Андрей", "Викторович", new Date(), "2222", "222222", "Λεωφόρος Βασιλέως Προκόπη, 09 4727 Λεμεσός", "+7(777)777-77-77", userRepository.findByUsername("DIRECTOR")));
        employeeList.add(new Employee("Андрюков", "Андрей", "Викторович", new Date(), "3333", "444444", "天津白云区", "+7(987)654-32-11", userRepository.findByUsername("MERCHANDISER")));
        employeeList.add(new Employee("Горбунов", "Антон", "Дмитриевич", new Date(), "0000", "666666", "074324, Атырау облысы, Маңғыстау қаласы, Абай көшесі, 782", "+7(999)666-55-44", userRepository.findByUsername("REPAIR_DEP")));
        employeeList.add(new Employee("Шестакова", "Ольга", "Николаевна", new Date(), "5555", "555555", "84709-584, Largo Leal, 715 São Hortência do Sul - AM", "+7(999)555-44-33", userRepository.findByUsername("CLIENT_DEP")));
    }

    public static void seed(EmployeeRepository employeeRepository, UserRepository userRepository) {
        init(userRepository);
        for (Employee employee : employeeList)
            if (employeeRepository.findByUserUsername(employee.getUser().getUsername()) == null)
                employeeRepository.save(employee);
    }
}