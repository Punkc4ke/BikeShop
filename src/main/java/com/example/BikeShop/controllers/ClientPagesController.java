package com.example.BikeShop.controllers;

import com.example.BikeShop.models.*;
import com.example.BikeShop.repositories.*;
import com.example.BikeShop.services.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@PreAuthorize("hasAnyAuthority('CLIENT')")
@Controller
public class ClientPagesController {

    private final ClientRepository clientRepository;

    private final ProductRepository productRepository;

    private final ChequeInfoRepository chequeInfoRepository;

    private final ChequeRepository chequeRepository;

    private final CartRepository cartRepository;

    private final ResourceLoader resourceLoader;

    public ClientPagesController(ClientRepository clientRepository, ProductRepository productRepository, ChequeInfoRepository chequeInfoRepository,
                                 ChequeRepository chequeRepository, CartRepository cartRepository, ResourceLoader resourceLoader) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.chequeInfoRepository = chequeInfoRepository;
        this.chequeRepository = chequeRepository;
        this.cartRepository = cartRepository;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("products")
    public String productIndex(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "clientPages/Products";
    }

    @GetMapping("search")
    public String productSearch(@RequestParam(required = false) String name, Model model) {
        Iterable<Product> products;
        if (name != null && !name.equals(""))
            products = productRepository.findByNameContains(name);
        else
            products = productRepository.findAll();
        model.addAttribute("products", products);
        return "clientPages/Products";
    }

    @GetMapping("sort")
    public String productSort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Product> products;
        if (sortType)
            products = productRepository.findAll(Sort.by(sortProperty).ascending());
        else
            products = productRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("products", products);
        return "clientPages/Products";
    }

    @GetMapping("history")
    public String history(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int chequesCount = 0;
        for (Cheque cheque : chequeRepository.findAll())
            chequesCount++;
        model.addAttribute("cheques", chequeRepository.findByChequeInfoClientUserUsername(authentication.getName()));
        model.addAttribute("chequesCount", chequesCount);
        return "clientPages/History";
    }

    @PostMapping("buy")
    public String buy(@RequestParam(required = false) Product product, @RequestParam(required = false) Cart cart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientRepository.findByUserUsername(authentication.getName());
        if (product != null) {
            ChequeInfo chequeInfo = new ChequeInfo(new Date(), client);
            chequeInfoRepository.save(chequeInfo);
            Cheque cheque = new Cheque(1, product, chequeInfo);
            chequeRepository.save(cheque);
            product.setCount(product.getCount() - 1);
            productRepository.save(product);
        } else if (cart != null) {
            ChequeInfo chequeInfo = new ChequeInfo(new Date(), client);
            Product productDB = cart.getProduct();
            chequeInfoRepository.save(chequeInfo);
            Cheque cheque = new Cheque(cart.getCount(), productDB, chequeInfo);
            chequeRepository.save(cheque);
            productDB.setCount(productDB.getCount() - cart.getCount());
            productRepository.save(productDB);
            cartRepository.delete(cart);
        }
        return "redirect:/history";
    }

    @GetMapping("buyAll")
    public String buyAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientRepository.findByUserUsername(authentication.getName());
        Iterable<Cart> carts = cartRepository.findByClientUserUsernameAndProductActive(authentication.getName(), true);
        ChequeInfo chequeInfo = new ChequeInfo(new Date(), client);
        for (Cart cart : carts) {
            Product productDB = cart.getProduct();
            chequeInfoRepository.save(chequeInfo);
            Cheque cheque = new Cheque(cart.getCount(), productDB, chequeInfo);
            chequeRepository.save(cheque);
            productDB.setCount(productDB.getCount() - cart.getCount());
            productRepository.save(productDB);
            cartRepository.delete(cart);
        }
        return "redirect:/history";
    }

    @GetMapping("generateExcel")
    public ResponseEntity<Resource> generateExcel() throws IOException {
        String currentDateTime = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new Date());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DocumentService excelExporter = new DocumentService((List<Cheque>) chequeRepository.findByChequeInfoClientUserUsername(authentication.getName()));
        excelExporter.exportExcel(currentDateTime);
        try {
            Resource resource = resourceLoader.getResource(Paths.get("files/cheque_" + currentDateTime + ".xlsx").toUri().toString());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("generateWord")
    public ResponseEntity<Resource> generateWord() throws IOException {
        String currentDateTime = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(new Date());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DocumentService wordExporter = new DocumentService((List<Cheque>) chequeRepository.findByChequeInfoClientUserUsername(authentication.getName()));
        wordExporter.exportWord(currentDateTime);
        try {
            Resource resource = resourceLoader.getResource(Paths.get("files/cheque_" + currentDateTime + ".docx").toUri().toString());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
