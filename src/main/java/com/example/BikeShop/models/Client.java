package com.example.BikeShop.models;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

//Клиент
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idClient;

    @Pattern(regexp = "[а-яА-Я]{1,30}", message = "Имя должно быть от 1 до 30 символов и состоять только из букв")
    private String name;

    @Pattern(regexp = "[а-яА-Я]{1,30}", message = "Фамилия должна быть от 1 до 30 символов и состоять только из букв")
    private String surname;

    @Pattern(regexp = "[а-яА-Я]{0,30}", message = "Отчество должно быть от 0 до 30 символов и состоять только из букв")
    private String patronymic;

    @Pattern(regexp = "[+]7[(]\\d{3}[)]\\d{3}-\\d{2}-\\d{2}", message = "Номера телефона должен состоять из 11 цифр и иметь данный формат: +7(999)999-99-99")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "idUser")
    private User user;

    public Client() {
    }

    public Client(String name, String surname, String patronymic, String phoneNumber, User user) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}