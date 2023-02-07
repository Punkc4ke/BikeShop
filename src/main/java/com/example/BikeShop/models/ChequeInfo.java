package com.example.BikeShop.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

//Информация чека
@Entity
public class ChequeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idChequeInfo;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date datePrint;

    @OneToOne
    @JoinColumn(name = "clientId", referencedColumnName = "idClient")
    private Client client;

    public ChequeInfo() {
    }

    public ChequeInfo(Date datePrint, Client client) {
        this.datePrint = datePrint;
        this.client = client;
    }

    public Long getIdChequeInfo() {
        return idChequeInfo;
    }

    public void setIdChequeInfo(Long idChequeInfo) {
        this.idChequeInfo = idChequeInfo;
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}