package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Cheque;
import com.example.BikeShop.models.Product;
import com.example.BikeShop.repositories.ChequeRepository;
import com.example.BikeShop.repositories.ProductRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PreAuthorize("hasAnyAuthority('SALES_DEP') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/report")
@Controller
public class ReportController {

    private final ChequeRepository chequeRepository;

    private final ProductRepository productRepository;

    public ReportController(ChequeRepository chequeRepository, ProductRepository productRepository) {
        this.chequeRepository = chequeRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("index")
    public String reportIndex(Model model) {
        model.addAttribute("products", getProductList(productRepository.findAll(), chequeRepository.findAll()));
        model.addAttribute("cheques", getChequeList(productRepository.findAll()));
        return "report/Index";
    }

    @GetMapping("filter")
    public String reportFilter(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date datePrintBegin,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date datePrintEnd,
                               Model model) {
        if (datePrintEnd == null && datePrintBegin == null) {
            model.addAttribute("products", getProductList(productRepository.findAll(), chequeRepository.findAll()));
            model.addAttribute("cheques", getChequeList(productRepository.findAll()));
            return "report/Index";
        }
        if (datePrintBegin != null) {
            model.addAttribute("products", getProductList(productRepository.findAll(),
                    (List) chequeRepository.findByChequeInfoDatePrintGreaterThan(datePrintBegin)));
            model.addAttribute("cheques", getChequeList(productRepository.findAll()));
            return "report/Index";
        }
        if (datePrintEnd != null) {
            model.addAttribute("products", getProductList(productRepository.findAll(),
                    (List) chequeRepository.findByChequeInfoDatePrintLessThan(datePrintEnd)));
            model.addAttribute("cheques", getChequeList(productRepository.findAll()));
            return "report/Index";
        }
        model.addAttribute("products", getProductList(productRepository.findAll(),
                (List) chequeRepository.findByChequeInfoDatePrintLessThanAndChequeInfoDatePrintGreaterThan(datePrintBegin, datePrintEnd)));
        model.addAttribute("cheques", getChequeList(productRepository.findAll()));
        return "report/Index";
    }

    private static List<String> getChequeList(List<Product> products) {
        List<String> productNames = new ArrayList<>();
        for (Product product : products)
            productNames.add(product.getName());
        return productNames;
    }

    private static List<Double> getProductList(List<Product> products, List<Cheque> cheques) {
        List<Double> soldProducts = new ArrayList<>();
        for (Product product : products) {
            double allSales = 0;
            for (var cheque : cheques)
                if (product == cheque.getProduct())
                    allSales += (double) cheque.getCount() * product.getPrice();
            soldProducts.add(allSales);
        }
        return soldProducts;
    }
}
