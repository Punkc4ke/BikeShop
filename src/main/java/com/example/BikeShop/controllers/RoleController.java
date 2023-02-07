package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Role;
import com.example.BikeShop.models.User;
import com.example.BikeShop.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoleController {

    private final UserRepository userRepository;

    public RoleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/main")
    public String getMainPage() {
        return switch (getRoleType(SecurityContextHolder.getContext().getAuthentication())) {
            case ADMIN, DIRECTOR -> "redirect:/database/index";
            case HR_DEP -> "redirect:/employee/index";
            case SALES_DEP -> "redirect:/report/index";
            case MERCHANDISER -> "redirect:/product/index";
            case REPAIR_DEP -> "redirect:/booking/index";
            case CLIENT_SERVICE_DEP -> "redirect:/client/index";
            default -> "redirect:/products";
        };
    }

    public Role getRoleType(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName());
        return (Role) user.getRoles().toArray()[0];
    }
}