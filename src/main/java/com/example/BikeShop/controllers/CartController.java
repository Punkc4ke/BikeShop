package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Cart;
import com.example.BikeShop.models.Client;
import com.example.BikeShop.models.Product;
import com.example.BikeShop.repositories.CartRepository;
import com.example.BikeShop.repositories.ClientRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyAuthority('CLIENT')")
@RequestMapping("/cart")
@Controller
public class CartController {

    private final CartRepository cartRepository;

    private final ClientRepository clientRepository;

    public CartController(CartRepository cartRepository, ClientRepository clientRepository) {
        this.cartRepository = cartRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("index")
    public String cartIndex(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Iterable<Cart> carts = cartRepository.findByClientUserUsername(authentication.getName());
        int cartCount = 0;
        for (Cart cart : carts) {
            if (cart.getCount() > cart.getProduct().getCount()) {
                cart.setCount(cart.getProduct().getCount());
                cartRepository.save(cart);
            }
            cartCount++;
        }
        model.addAttribute("carts", carts);
        model.addAttribute("cartsCount", cartCount);
        return "cart/Index";
    }

    @PostMapping("create")
    public String cartCreate(@RequestParam Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientRepository.findByUserUsername(authentication.getName());
        Cart cart;
        if (cartRepository.findByClientAndProduct(client, product) == null)
            cart = new Cart(1, client, product);
        else {
            cart = cartRepository.findByClientAndProduct(client, product);
            cart.setCount(cart.getCount() + 1);
        }
        cartRepository.save(cart);
        return "redirect:/products";
    }

    @PostMapping("edit")
    public String cartEdit(@RequestParam Cart cart, @RequestParam int count) {
        cart.setCount(count);
        cartRepository.save(cart);
        return "redirect:/cart/index";
    }

    @PostMapping("delete")
    public String cartDelete(@RequestParam Cart cart) {
        cartRepository.delete(cart);
        return "redirect:/cart/index";
    }
}
