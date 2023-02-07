package com.example.BikeShop.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

//Чек
@Entity
public class Cheque {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCheque;

    @NotNull
    private int count;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "chequeInfoId", referencedColumnName = "idChequeInfo")
    private ChequeInfo chequeInfo;

    public Cheque() {
    }

    public Cheque(int count, Product product, ChequeInfo chequeInfo) {
        this.count = count;
        this.product = product;
        this.chequeInfo = chequeInfo;
    }

    public Long getIdCheque() {
        return idCheque;
    }

    public void setIdCheque(Long idCheque) {
        this.idCheque = idCheque;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ChequeInfo getChequeInfo() {
        return chequeInfo;
    }

    public void setChequeInfo(ChequeInfo chequeInfo) {
        this.chequeInfo = chequeInfo;
    }
}