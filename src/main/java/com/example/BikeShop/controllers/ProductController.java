package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Product;
import com.example.BikeShop.repositories.*;
import com.example.BikeShop.services.ProductImageService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@PreAuthorize("hasAnyAuthority('MERCHANDISER') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/product")
@Controller
public class ProductController {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ColorRepository colorRepository;

    private final StorageRepository storageRepository;

    private final SupplierRepository supplierRepository;

    private final ImageRepository imageRepository;

    private final ProductImageService productImageService;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, ColorRepository colorRepository,
                             StorageRepository storageRepository, SupplierRepository supplierRepository, ImageRepository imageRepository,
                             ProductImageService productImageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.storageRepository = storageRepository;
        this.supplierRepository = supplierRepository;
        this.imageRepository = imageRepository;
        this.productImageService = productImageService;
    }


    @GetMapping("index")
    public String productIndex(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/Index";
    }

    @GetMapping("search")
    public String productSearch(@RequestParam(required = false) String name, Model model) {
        Iterable<Product> products;
        if (name != null && !name.equals(""))
            products = productRepository.findByNameContains(name);
        else
            products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product/Index";
    }

    @GetMapping("sort")
    public String productSort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Product> products;
        if (sortType)
            products = productRepository.findAll(Sort.by(sortProperty).ascending());
        else
            products = productRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("products", products);
        return "product/Index";
    }

    @GetMapping("create")
    public String productCreate(@ModelAttribute("product") Product product, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("colors", colorRepository.findAll());
        model.addAttribute("storages", storageRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "product/Create";
    }

    @GetMapping("edit")
    public String productEdit(@RequestParam Product product, Model model) {
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("colors", colorRepository.findAll());
        model.addAttribute("storages", storageRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "product/Edit";
    }

    @GetMapping("details")
    public String productDetails(@RequestParam Product product, Model model) {
        model.addAttribute("product", product);
        model.addAttribute("image", imageRepository.findImageByProduct(product));
        return "product/Details";
    }

    @PostMapping("create")
    public String productCreate(@ModelAttribute("product") @Valid Product product, BindingResult bindingResultProduct,
                                @RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            bindingResultProduct.addError(new ObjectError("image", "Изображение товара не должно быть пустым"));
            model.addAttribute("errorMessageImage", "Изображение товара не должно быть пустым");
        }
        if (bindingResultProduct.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("colors", colorRepository.findAll());
            model.addAttribute("storages", storageRepository.findAll());
            model.addAttribute("suppliers", supplierRepository.findAll());
            return "product/Create";
        }
        product.setActive(true);
        productImageService.saveProductAndImage(product, file);
        return "redirect:/product/index";
    }

    @PostMapping("edit")
    public String productEdit(@ModelAttribute("product") @Valid Product product, BindingResult bindingResultProduct,
                              @RequestParam(required = false, name = "file") MultipartFile file, Model model) throws IOException {
        if (bindingResultProduct.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("colors", colorRepository.findAll());
            model.addAttribute("storages", storageRepository.findAll());
            model.addAttribute("suppliers", supplierRepository.findAll());
            return "product/Edit";
        }
        productImageService.saveProductAndImage(product, file);
        return "redirect:/product/index";
    }

    @PostMapping("changeStatus")
    public String productChangeStatus(@RequestParam Product product) {
        product.setActive(!product.isActive());
        productRepository.save(product);
        return "redirect:/product/index";
    }
}
