package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Supplier;
import com.example.BikeShop.repositories.SupplierRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasAnyAuthority('MERCHANDISER') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/supplier")
@Controller
public class SupplierController {

    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @GetMapping("index")
    public String supplierIndex(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "supplier/Index";
    }

    @GetMapping("search")
    public String supplierSearch(@RequestParam(required = false) String nameOrganization, Model model) {
        Iterable<Supplier> suppliers;
        if (nameOrganization != null && !nameOrganization.equals(""))
            suppliers = supplierRepository.findByNameOrganization(nameOrganization);
        else
            suppliers = supplierRepository.findAll();
        model.addAttribute("suppliers", suppliers);
        return "supplier/Index";
    }

    @GetMapping("sort")
    public String supplierSort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Supplier> suppliers;
        if (sortType)
            suppliers = supplierRepository.findAll(Sort.by(sortProperty).ascending());
        else
            suppliers = supplierRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("suppliers", suppliers);
        return "supplier/Index";
    }

    @GetMapping("create")
    public String supplierCreate(@ModelAttribute("supplier") Supplier supplier) {
        return "supplier/Create";
    }

    @GetMapping("edit")
    public String supplierEdit(@RequestParam Supplier supplier, Model model) {
        model.addAttribute("supplier", supplier);
        return "supplier/Edit";
    }

    @PostMapping("create")
    public String supplierCreate(@ModelAttribute("supplier") @Valid Supplier supplier, BindingResult bindingResultSupplier, Model model) {
        if (supplierRepository.findByPhoneNumber(supplier.getPhoneNumber()) != null) {
            bindingResultSupplier.addError(new ObjectError("phoneNumber", "Данный номер телефона уже занят"));
            model.addAttribute("errorMessagePhoneNumber", "Данный номер телефона уже занят");
        }
        if (bindingResultSupplier.hasErrors())
            return "supplier/Create";
        supplierRepository.save(supplier);
        return "redirect:/supplier/index";
    }

    @PostMapping("edit")
    public String supplierEdit(@ModelAttribute("supplier") @Valid Supplier supplier, BindingResult bindingResultSupplier, Model model) {
        if (supplierRepository.findByPhoneNumber(supplier.getPhoneNumber()) != null &&
                !supplierRepository.findByPhoneNumber(supplier.getPhoneNumber())
                        .getIdSupplier()
                        .equals(supplier.getIdSupplier())) {
            bindingResultSupplier.addError(new ObjectError("phoneNumber", "Данный номер телефона уже занят"));
            model.addAttribute("errorMessagePhoneNumber", "Данный номер телефона уже занят");
        }
        if (bindingResultSupplier.hasErrors())
            return "supplier/Edit";
        supplierRepository.save(supplier);
        return "redirect:/supplier/index";
    }
}
