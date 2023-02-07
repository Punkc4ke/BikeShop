package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Color;
import com.example.BikeShop.repositories.ColorRepository;

import java.util.ArrayList;
import java.util.List;

public class ColorSeeder {

    private static final List<Color> colorList = new ArrayList<>();

    private static void init() {
        colorList.add(new Color("Красный"));
        colorList.add(new Color("Оранжевый"));
        colorList.add(new Color("Желтый"));
        colorList.add(new Color("Зеленый"));
        colorList.add(new Color("Голубой"));
        colorList.add(new Color("Синий"));
        colorList.add(new Color("Фиолетовый"));
        colorList.add(new Color("Розовый"));
        colorList.add(new Color("Черный"));
        colorList.add(new Color("Белый"));
        colorList.add(new Color("Коричневый"));
    }

    public static void seed(ColorRepository colorRepository) {
        init();
        for (Color color : colorList)
            if (colorRepository.findByName(color.getName()) == null)
                colorRepository.save(color);
    }
}
