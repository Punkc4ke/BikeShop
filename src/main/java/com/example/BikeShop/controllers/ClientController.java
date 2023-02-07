package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Client;
import com.example.BikeShop.models.Role;
import com.example.BikeShop.models.User;
import com.example.BikeShop.repositories.ClientRepository;
import com.example.BikeShop.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@PreAuthorize("hasAnyAuthority('CLIENT_SERVICE_DEP') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/client")
@Controller
public class ClientController {

    private final ClientRepository clientRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ClientController(ClientRepository clientRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("index")
    public String clientIndex(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "client/Index";
    }

    @GetMapping("search")
    public String clientSearch(@RequestParam(required = false) String username, Model model) {
        Iterable<Client> clients;
        if (username != null && !username.equals(""))
            clients = clientRepository.findByUserUsernameContains(username);
        else
            clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "client/Index";
    }

    @GetMapping("sort")
    public String clientSort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Client> clients;
        if (sortType)
            clients = clientRepository.findAll(Sort.by(sortProperty).ascending());
        else
            clients = clientRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("clients", clients);
        return "client/Index";
    }

    @GetMapping("create")
    public String clientCreate(@ModelAttribute("user") User user, @ModelAttribute("client") Client client) {
        return "client/Create";
    }

    @GetMapping("edit")
    public String clientEdit(@RequestParam Client client, Model model) {
        User user = client.getUser();
        model.addAttribute("client", client);
        model.addAttribute("user", user);
        return "client/Edit";
    }

    @GetMapping("details")
    public String clientDetails(@RequestParam Client client, Model model) {
        model.addAttribute("client", client);
        return "client/Details";
    }

    @PostMapping("create")
    public String clientCreate(@ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
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
            return "client/Create";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.CLIENT));
        userRepository.save(user);
        client.setUser(user);
        clientRepository.save(client);
        return "redirect:/client/index";
    }

    @PostMapping("edit")
    public String clientEdit(@ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                             @ModelAttribute("client") @Valid Client client, BindingResult bindingResultClient,
                             Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null &&
                !userRepository.findByUsername(user.getUsername()).getIdUser().equals(user.getIdUser())) {
            bindingResultUser.addError(new ObjectError("username", "Данный логин уже занят"));
            model.addAttribute("errorMessageUsername", "Данный логин уже занят");
        }
        if (bindingResultUser.hasErrors() || bindingResultClient.hasErrors())
            return "client/Edit";
        userRepository.save(user);
        client.setUser(user);
        clientRepository.save(client);
        return "redirect:/client/index";
    }

    @PostMapping("changeStatus")
    public String clientChangeStatus(@RequestParam Client client) {
        User user = client.getUser();
        user.setActive(!user.isActive());
        userRepository.save(user);
        return "redirect:/client/index";
    }
}
