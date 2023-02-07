package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Status;
import com.example.BikeShop.repositories.StatusRepository;

import java.util.ArrayList;
import java.util.List;

public class StatusSeeder {

    private static final List<Status> statusList = new ArrayList<>();

    private static void init() {
        statusList.add(new Status("Ожидает подтверждения"));
        statusList.add(new Status("Подтвержден"));
        statusList.add(new Status("В процессе"));
        statusList.add(new Status("Готов к выдаче"));
        statusList.add(new Status("Ошибка подтверждения"));
    }

    public static void seed(StatusRepository statusRepository) {
        init();
        for (Status status : statusList)
            if (statusRepository.findByName(status.getName()) == null)
                statusRepository.save(status);
    }
}