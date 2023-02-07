package com.example.BikeShop.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

//Корзина
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCart;

    @Min(value = 1, message = "Количество товара должно быть больше или равно 1")
    @NotNull(message = "Количество товара не должно быть пустым")
    private int count;

    @ManyToOne
    @JoinColumn(name = "clientId", referencedColumnName = "idClient")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "idProduct")
    private Product product;

    public Cart() {
    }

    public Cart(int count, Client client, Product product) {
        this.count = count;
        this.client = client;
        this.product = product;
    }

    public Long getIdCart() {
        return idCart;
    }

    public void setIdCart(Long idCart) {
        this.idCart = idCart;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
