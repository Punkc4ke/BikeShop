package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Storage;
import com.example.BikeShop.repositories.StorageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasAnyAuthority('MERCHANDISER') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/storage")
@Controller
public class StorageController {

    private final StorageRepository storageRepository;

    public StorageController(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @GetMapping("index")
    public String storageIndex(Model model) {
        model.addAttribute("storages", storageRepository.findAll());
        return "storage/Index";
    }

    @GetMapping("search")
    public String storageSearch(@RequestParam(required = false) String address, Model model) {
        Iterable<Storage> storages;
        if (address != null && !address.equals(""))
            storages = storageRepository.findByAddressContains(address);
        else
            storages = storageRepository.findAll();
        model.addAttribute("storages", storages);
        return "storage/Index";
    }

    @GetMapping("sort")
    public String storageSort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Storage> storages;
        if (sortType)
            storages = storageRepository.findAll(Sort.by(sortProperty).ascending());
        else
            storages = storageRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("storages", storages);
        return "storage/Index";
    }

    @GetMapping("create")
    public String storageCreate(@ModelAttribute("storage") Storage storage) {
        return "storage/Create";
    }

    @GetMapping("edit")
    public String storageEdit(@RequestParam Storage storage, Model model) {
        model.addAttribute("storage", storage);
        return "storage/Edit";
    }

    @PostMapping("create")
    public String storageCreate(@ModelAttribute("storage") @Valid Storage storage, BindingResult bindingResultStorage, Model model) {
        if (storageRepository.findByAddress(storage.getAddress()) != null) {
            bindingResultStorage.addError(new ObjectError("address", "Данный склад уже существует"));
            model.addAttribute("errorMessageAddress", "Данный склад уже существует");
        }
        if (bindingResultStorage.hasErrors())
            return "storage/Create";
        storageRepository.save(storage);
        return "redirect:/storage/index";
    }

    @PostMapping("edit")
    public String storageEdit(@ModelAttribute("storage") @Valid Storage storage, BindingResult bindingResultStorage, Model model) {
        if (storageRepository.findByAddress(storage.getAddress()) != null &&
                !storageRepository.findByAddress(storage.getAddress())
                        .getIdStorage()
                        .equals(storage.getIdStorage())) {
            bindingResultStorage.addError(new ObjectError("address", "Данный склад уже существует"));
            model.addAttribute("errorMessageAddress", "Данный склад уже существует");
        }
        if (bindingResultStorage.hasErrors())
            return "storage/Edit";
        storageRepository.save(storage);
        return "redirect:/storage/index";
    }
}
