package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Client;
import com.example.BikeShop.models.Role;
import com.example.BikeShop.models.User;
import com.example.BikeShop.repositories.ClientRepository;
import com.example.BikeShop.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    public RegistrationController(UserRepository userRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user, @ModelAttribute("client") Client client) {
        return "user/Create";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                               @ModelAttribute("client") @Valid Client client, BindingResult bindingResultClient,
                               @RequestParam String passwordSubmit, Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            bindingResultUser.addError(new ObjectError("username", "Данный логин уже занят"));
            model.addAttribute("errorMessageUsername", "Данный логин уже занят");
        }
        if (!passwordSubmit.equals(user.getPassword())) {
            bindingResultUser.addError(new ObjectError("password", "Пароли не совпадают"));
            model.addAttribute("errorMessagePassword", "Пароли не совпадают");
        }
        if (bindingResultUser.hasErrors() || bindingResultClient.hasErrors())
            return "user/Create";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.CLIENT));
        userRepository.save(user);
        client.setUser(user);
        clientRepository.save(client);
        return "redirect:/login";
    }
}