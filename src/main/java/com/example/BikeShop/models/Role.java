package com.example.BikeShop.models;

import org.springframework.security.core.GrantedAuthority;

//Перечисление ролей
public enum Role implements GrantedAuthority {

    ADMIN("Администратор"),
    HR_DEP("Сотрудник отдела кадров"),
    SALES_DEP("Сотрудник отдела продаж"),
    DIRECTOR("Директор"), // Суперпользователь
    MERCHANDISER("Товаровед"),
    REPAIR_DEP("Сотрудник отдела ремонта"),
    CLIENT_SERVICE_DEP("Сотрудник отдела по работе с клиентами"),
    CLIENT("Клиент");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}