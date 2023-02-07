package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Role;
import com.example.BikeShop.models.User;
import com.example.BikeShop.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserSeeder {

    private static final List<User> userList = new ArrayList<>();

    private static void init() {
        userList.add(new User("CLIENT1", new BCryptPasswordEncoder().encode("CLIENT1"), true, Collections.singleton(Role.CLIENT)));
        userList.add(new User("CLIENT2", new BCryptPasswordEncoder().encode("CLIENT2"), true, Collections.singleton(Role.CLIENT)));
        userList.add(new User("CLIENT3", new BCryptPasswordEncoder().encode("CLIENT3"), true, Collections.singleton(Role.CLIENT)));
        userList.add(new User("CLIENT4", new BCryptPasswordEncoder().encode("CLIENT4"), true, Collections.singleton(Role.CLIENT)));
        userList.add(new User("CLIENT5", new BCryptPasswordEncoder().encode("CLIENT5"), true, Collections.singleton(Role.CLIENT)));

        userList.add(new User("ADMIN", new BCryptPasswordEncoder().encode("ADMIN"), true, Collections.singleton(Role.ADMIN)));
        userList.add(new User("HR_DEP", new BCryptPasswordEncoder().encode("HR_DEP"), true, Collections.singleton(Role.HR_DEP)));
        userList.add(new User("SALES_DEP", new BCryptPasswordEncoder().encode("SALES_DEP"), true, Collections.singleton(Role.SALES_DEP)));
        userList.add(new User("DIRECTOR", new BCryptPasswordEncoder().encode("DIRECTOR"), true, Collections.singleton(Role.DIRECTOR)));
        userList.add(new User("MERCHANDISER", new BCryptPasswordEncoder().encode("MERCHANDISER"), true, Collections.singleton(Role.MERCHANDISER)));
        userList.add(new User("REPAIR_DEP", new BCryptPasswordEncoder().encode("REPAIR_DEP"), true, Collections.singleton(Role.REPAIR_DEP)));
        userList.add(new User("CLIENT_DEP", new BCryptPasswordEncoder().encode("CLIENT_SERVICE_DEP"), true, Collections.singleton(Role.CLIENT_SERVICE_DEP)));
    }

    public static void seed(UserRepository userRepository) {
        init();
        for (User user : userList) {
            if (userRepository.findByUsername(user.getUsername()) == null)
                userRepository.save(user);
        }
    }
}
