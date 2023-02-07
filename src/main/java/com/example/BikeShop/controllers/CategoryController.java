package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Category;
import com.example.BikeShop.repositories.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasAnyAuthority('MERCHANDISER') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/category")
@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("index")
    public String categoryIndex(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "category/Index";
    }

    @GetMapping("search")
    public String categorySearch(@RequestParam(required = false) String name, Model model) {
        Iterable<Category> categories;
        if (name != null && !name.equals(""))
            categories = categoryRepository.findByNameContains(name);
        else
            categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "category/Index";
    }

    @GetMapping("sort")
    public String categorySort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Category> categories;
        if (sortType)
            categories = categoryRepository.findAll(Sort.by(sortProperty).ascending());
        else
            categories = categoryRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("categories", categories);
        return "category/Index";
    }

    @GetMapping("create")
    public String categoryCreate(@ModelAttribute("category") Category category) {
        return "category/Create";
    }

    @GetMapping("edit")
    public String categoryEdit(@RequestParam Category category, Model model) {
        model.addAttribute("category", category);
        return "category/Edit";
    }

    @PostMapping("create")
    public String categoryCreate(@ModelAttribute("category") @Valid Category category, BindingResult bindingResultCategory, Model model) {
        if (categoryRepository.findByName(category.getName()) != null) {
            bindingResultCategory.addError(new ObjectError("name", "Данная категория уже существует"));
            model.addAttribute("errorMessageName", "Данная категория уже существует");
        }
        if (bindingResultCategory.hasErrors())
            return "category/Create";
        categoryRepository.save(category);
        return "redirect:/category/index";
    }

    @PostMapping("edit")
    public String categoryEdit(@ModelAttribute("category") @Valid Category category, BindingResult bindingResultCategory, Model model) {
        if (categoryRepository.findByName(category.getName()) != null &&
                !categoryRepository.findByName(category.getName())
                        .getIdCategory()
                        .equals(category.getIdCategory())) {
            bindingResultCategory.addError(new ObjectError("name", "Данная категория уже существует"));
            model.addAttribute("errorMessageName", "Данная категория уже существует");
        }
        if (bindingResultCategory.hasErrors())
            return "category/Edit";
        categoryRepository.save(category);
        return "redirect:/category/index";
    }
}