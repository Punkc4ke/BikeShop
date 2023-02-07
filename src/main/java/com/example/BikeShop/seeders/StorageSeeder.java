package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Storage;
import com.example.BikeShop.repositories.StorageRepository;

import java.util.ArrayList;
import java.util.List;

public class StorageSeeder {

    private static final List<Storage> storageList = new ArrayList<>();

    private static void init() {
        storageList.add(new Storage("046653, Тюменская область, город Ступино, бульвар Балканская, 67"));
        storageList.add(new Storage("879450, Ульяновская область, город Сергиев Посад, шоссе Космонавтов, 90"));
        storageList.add(new Storage("086597, Тамбовская область, город Домодедово, бульвар Балканская, 13"));
        storageList.add(new Storage("550196, Читинская область, город Ступино, пр. Ленина, 35"));
        storageList.add(new Storage("070007, Волгоградская область, город Павловский Посад, спуск Сталина, 09"));
    }

    public static void seed(StorageRepository storageRepository) {
        init();
        for (Storage storage : storageList)
            if (storageRepository.findByAddress(storage.getAddress()) == null)
                storageRepository.save(storage);
    }
}
