package com.example.BikeShop.seeders;

import com.example.BikeShop.models.Category;
import com.example.BikeShop.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class CategorySeeder {

    private static final List<Category> categoryList = new ArrayList<>();

    private static void init() {
        categoryList.add(new Category("Спортивный"));
        categoryList.add(new Category("Горный"));
        categoryList.add(new Category("Детский"));
        categoryList.add(new Category("Стандарт"));
        categoryList.add(new Category("Электро"));
    }

    public static void seed(CategoryRepository categoryRepository) {
        init();
        for (Category category : categoryList)
            if (categoryRepository.findByName(category.getName()) == null)
                categoryRepository.save(category);
    }
}
