package com.example.BikeShop.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

//Поставщик
@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSupplier;

    @Pattern(regexp = "[а-яА-Я]{1,50}", message = "Наименование организации должно быть от 1 до 50 символов и состоять только из букв")
    private String nameOrganization;

    @Pattern(regexp = "[+]7[(]\\d{3}[)]\\d{3}-\\d{2}-\\d{2}", message = "Номера телефона должен состоять из 11 цифр и иметь данный формат: +7(999)999-99-99")
    private String phoneNumber;

    public Supplier() {
    }

    public Supplier(String nameOrganization, String phoneNumber) {
        this.nameOrganization = nameOrganization;
        this.phoneNumber = phoneNumber;
    }

    public Long getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Long idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getNameOrganization() {
        return nameOrganization;
    }

    public void setNameOrganization(String nameOrganization) {
        this.nameOrganization = nameOrganization;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}