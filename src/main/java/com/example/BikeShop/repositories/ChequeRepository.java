package com.example.BikeShop.repositories;

import com.example.BikeShop.models.Cheque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ChequeRepository extends JpaRepository<Cheque, Long> {

    Iterable<Cheque> findByChequeInfoClientUserUsername(String username);

    Iterable<Cheque> findByChequeInfoDatePrintLessThanAndChequeInfoDatePrintGreaterThan(Date datePrintBegin, Date datePrintEnd);

    Iterable<Cheque> findByChequeInfoDatePrintGreaterThan(Date datePrintBegin);

    Iterable<Cheque> findByChequeInfoDatePrintLessThan(Date datePrint);
}