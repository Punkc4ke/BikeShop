package com.example.BikeShop.controllers;

import com.example.BikeShop.models.Booking;
import com.example.BikeShop.models.Malfunction;
import com.example.BikeShop.models.Status;
import com.example.BikeShop.repositories.*;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasAnyAuthority('REPAIR_DEP') or hasAnyAuthority('DIRECTOR')")
@RequestMapping("/booking")
@Controller
public class BookingController {

    private final BookingRepository bookingRepository;

    private final ProductRepository productRepository;

    private final StatusRepository statusRepository;

    private final ClientRepository clientRepository;

    private final EmployeeRepository employeeRepository;

    private final MalfunctionRepository malfunctionRepository;

    public BookingController(BookingRepository bookingRepository, ProductRepository productRepository, StatusRepository statusRepository,
                             ClientRepository clientRepository, EmployeeRepository employeeRepository, MalfunctionRepository malfunctionRepository) {
        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
        this.statusRepository = statusRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.malfunctionRepository = malfunctionRepository;
    }

    @GetMapping("index")
    public String bookingIndex(Model model) {
        model.addAttribute("bookings", bookingRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("malfunctions", malfunctionRepository.findAll());
        return "booking/Index";
    }

    @GetMapping("search")
    public String bookingSearch(@RequestParam(required = false) String name, Model model) {
        Iterable<Booking> bookings;
        if (name != null && !name.equals(""))
            bookings = bookingRepository.findByProductNameContains(name);
        else
            bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        model.addAttribute("statuses", statusRepository.findAll());
        return "booking/Index";
    }

    @GetMapping("sort")
    public String bookingSort(@RequestParam String sortProperty, @RequestParam boolean sortType, Model model) {
        Iterable<Booking> bookings;
        if (sortType)
            bookings = bookingRepository.findAll(Sort.by(sortProperty).ascending());
        else
            bookings = bookingRepository.findAll(Sort.by(sortProperty).descending());
        model.addAttribute("bookings", bookings);
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("malfunctions", malfunctionRepository.findAll());
        return "booking/Index";
    }

    @GetMapping("create")
    public String bookingCreate(@ModelAttribute("booking") Booking booking, Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        return "booking/Create";
    }

    @GetMapping("edit")
    public String bookingEdit(@RequestParam Booking booking, Model model) {
        model.addAttribute("booking", booking);
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("malfunctions", malfunctionRepository.findAll());
        return "booking/Edit";
    }

    @GetMapping("details")
    public String bookingDetails(@RequestParam Booking booking, Model model) {
        model.addAttribute("booking", booking);
        return "booking/Details";
    }

    @PostMapping("create")
    public String bookingCreate(@ModelAttribute("booking") @Valid Booking booking, BindingResult bindingResultBooking, Model model) {
        if (booking.getDateEnd() != null && booking.getDateBegin().after(booking.getDateEnd())) {
            bindingResultBooking.addError(new ObjectError("dateEnd", "Дата начала работ не должна быть позже даты завершения работ"));
            model.addAttribute("errorMessageDateEnd", "Дата начала работ не должна быть позже даты завершения работ");
        }
        if (bindingResultBooking.hasErrors()) {
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("statuses", statusRepository.findAll());
            model.addAttribute("clients", clientRepository.findAll());
            return "booking/Create";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        booking.setEmployee(employeeRepository.findByUserUsername(authentication.getName()));
        bookingRepository.save(booking);
        return "redirect:/booking/index";
    }

    @PostMapping("edit")
    public String bookingEdit(@ModelAttribute("booking") @Valid Booking booking, BindingResult bindingResultBooking, Model model) {
        if (booking.getDateEnd() != null && booking.getDateBegin().after(booking.getDateEnd())) {
            bindingResultBooking.addError(new ObjectError("dateEnd", "Дата начала работ не должна быть позже даты завершения работ"));
            model.addAttribute("errorMessageDateEnd", "Дата начала работ не должна быть позже даты завершения работ");
        }
        if (bindingResultBooking.hasErrors()) {
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("clients", clientRepository.findAll());
            model.addAttribute("malfunctions", malfunctionRepository.findAll());
            return "booking/Edit";
        }
        booking.setMalfunctionList(bookingRepository.findById(booking.getIdBooking()).get().getMalfunctionList());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        booking.setEmployee(employeeRepository.findByUserUsername(authentication.getName()));
        bookingRepository.save(booking);
        return "redirect:/booking/index";
    }

    @PostMapping("edit/malfunction")
    public String bookingMalfunctionEdit(@RequestParam Booking booking, @RequestParam List<Malfunction> malfunctions) {
        booking.setMalfunctionList(malfunctions);
        bookingRepository.save(booking);
        return "redirect:/booking/index";
    }

    @PostMapping("changeStatus")
    public String bookingChangeStatus(@RequestParam Booking booking, @RequestParam Status status) {
        booking.setStatus(status);
        bookingRepository.save(booking);
        return "redirect:/booking/index";
    }

    @PostMapping("delete")
    public String bookingDelete(@RequestParam Booking booking) {
        bookingRepository.delete(booking);
        return "redirect:/booking/index";
    }
}
