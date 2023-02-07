package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Employee;
import com.example.BikeShop.models.Role;
import com.example.BikeShop.models.User;
import com.example.BikeShop.repositories.EmployeeRepository;
import com.example.BikeShop.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PreAuthorize("hasAnyAuthority('HR_DEP') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/employee")
@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeRepository employeeRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("index")
    public String employeeIndex(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("employees", employeeRepository.findByUserUsernameNot(authentication.getName()));
        return "employee/Index";
    }

    @GetMapping("search")
    public String employeeSearch(@RequestParam(required = false) String username, Model model) {
        Iterable<Employee> employees;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (username != null && !username.equals(""))
            employees = employeeRepository.findByUserUsernameContainsAndUserUsernameNot(username, authentication.getName());
        else
            employees = employeeRepository.findByUserUsernameNot(authentication.getName());
        model.addAttribute("employees", employees);
        return "employee/Index";
    }

    @GetMapping("sort")
    public String employeeSort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Employee> employees;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (sortType)
            employees = employeeRepository.findAllByUserUsernameNot(authentication.getName(), Sort.by(sortProperty).ascending());
        else
            employees = employeeRepository.findAllByUserUsernameNot(authentication.getName(), Sort.by(sortProperty).descending());
        model.addAttribute("employees", employees);
        return "employee/Index";
    }

    @GetMapping("create")
    public String employeeCreate(@ModelAttribute("user") User user, @ModelAttribute("employee") Employee employee, Model model) {
        model.addAttribute("roles", getEmployeeRoles());
        return "employee/Create";
    }

    @GetMapping("edit")
    public String employeeEdit(@RequestParam Employee employee, Model model) {
        User user = employee.getUser();
        model.addAttribute("roles", getEmployeeRoles());
        model.addAttribute("employee", employee);
        model.addAttribute("user", user);
        return "employee/Edit";
    }

    @GetMapping("details")
    public String employeeDetails(@RequestParam Employee employee, Model model) {
        model.addAttribute("employee", employee);
        model.addAttribute("role", employee.getUser().getRoles());
        return "employee/Details";
    }

    @PostMapping("create")
    public String employeeCreate(@ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                                 @ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResultEmployee,
                                 @RequestParam String passwordSubmit, Model model) {
        if (employee.getDateBirth() != null) {
            long milliseconds = new Date().getTime() - employee.getDateBirth().getTime();
            int ages = (int) (milliseconds / (24 * 60 * 60 * 1000 * 365.25));
            if (userRepository.findByUsername(user.getUsername()) != null) {
                bindingResultUser.addError(new ObjectError("username", "Данный логин уже занят"));
                model.addAttribute("errorMessageUsername", "Данный логин уже занят");
            }
            if (ages < 16) {
                bindingResultEmployee.addError(new ObjectError("dateBirth", "Возраст меньше 16"));
                model.addAttribute("errorMessageDateBirth", "Возраст меньше 16");
            }
            if (!passwordSubmit.equals(user.getPassword())) {
                bindingResultUser.addError(new ObjectError("password", "Пароли не совпадают"));
                model.addAttribute("errorMessagePassword", "Пароли не совпадают");
            }
        }
        if (bindingResultUser.hasErrors() || bindingResultEmployee.hasErrors()) {
            model.addAttribute("roles", getEmployeeRoles());
            return "employee/Create";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        userRepository.save(user);
        employee.setUser(user);
        employeeRepository.save(employee);
        return "redirect:/employee/index";
    }

    @PostMapping("edit")
    public String employeeEdit(@ModelAttribute("user") @Valid User user, BindingResult bindingResultUser,
                               @ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResultEmployee,
                               Model model) {
        if (employee.getDateBirth() != null) {
            long milliseconds = new Date().getTime() - employee.getDateBirth().getTime();
            int ages = (int) (milliseconds / (24 * 60 * 60 * 1000 * 365.25));
            if (userRepository.findByUsername(user.getUsername()) != null &&
                    !userRepository.findByUsername(user.getUsername()).getIdUser().equals(user.getIdUser())) {
                bindingResultUser.addError(new ObjectError("username", "Данный логин уже занят"));
                model.addAttribute("errorMessageUsername", "Данный логин уже занят");
            }
            if (ages < 16) {
                bindingResultEmployee.addError(new ObjectError("dateBirth", "Возраст меньше 16"));
                model.addAttribute("errorMessageDateBirth", "Возраст меньше 16");
            }
        }
        if (bindingResultUser.hasErrors() || bindingResultEmployee.hasErrors()) {
            model.addAttribute("roles", getEmployeeRoles());
            return "employee/Edit";
        }
        userRepository.save(user);
        employee.setUser(user);
        employeeRepository.save(employee);
        return "redirect:/employee/index";
    }

    @PostMapping("changeStatus")
    public String employeeChangeStatus(@RequestParam Employee employee) {
        User user = employee.getUser();
        user.setActive(!user.isActive());
        userRepository.save(user);
        return "redirect:/employee/index";
    }

    private List<Role> getEmployeeRoles() {
        List<Role> roles = new ArrayList<>(List.of(Role.values()));
        roles.remove(Role.CLIENT);
        return roles;
    }
}