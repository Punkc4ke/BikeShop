package com.example.BikeShop.models;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

//Неисправность
@Entity
public class Malfunction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMalfunction;

    private String name;

    @ManyToMany
    @JoinTable(name = "BookingMalfunction", joinColumns = @JoinColumn(name = "malfunctionId"), inverseJoinColumns = @JoinColumn(name = "bookingId"))
    private List<Booking> bookingList;

    public Malfunction() {
    }

    public Malfunction(String name) {
        this.name = name;
    }

    public Long getIdMalfunction() {
        return idMalfunction;
    }

    public void setIdMalfunction(Long idMalfunction) {
        this.idMalfunction = idMalfunction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}