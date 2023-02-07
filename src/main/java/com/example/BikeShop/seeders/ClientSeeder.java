package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Client;
import com.example.BikeShop.repositories.ClientRepository;
import com.example.BikeShop.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ClientSeeder {

    private static final List<Client> clientList = new ArrayList<>();

    private static void init(UserRepository userRepository) {
        clientList.add(new Client("Иванов", "Иван", "Иванович", "+7(999)999-91-91", userRepository.findByUsername("CLIENT1")));
        clientList.add(new Client("Петров", "Петр", "Петрович", "+7(999)999-92-92", userRepository.findByUsername("CLIENT2")));
        clientList.add(new Client("Антонов", "Антон", "Антонович", "+7(999)999-93-93", userRepository.findByUsername("CLIENT3")));
        clientList.add(new Client("Михайлов", "Михаил", "Михайлович", "+7(999)999-94-94", userRepository.findByUsername("CLIENT4")));
        clientList.add(new Client("Андреев", "Андрей", "Андреевич", "+7(999)999-95-95", userRepository.findByUsername("CLIENT5")));
    }

    public static void seed(ClientRepository clientRepository, UserRepository userRepository) {
        init(userRepository);
        for (Client client : clientList)
            if (clientRepository.findByUserUsername(client.getUser().getUsername()) == null)
                clientRepository.save(client);
    }
}