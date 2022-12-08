package com.example.attestationspring.controllers;

import com.example.attestationspring.models.Person;
import com.example.attestationspring.services.PersonService;
import com.example.attestationspring.services.ProductService;
import com.example.attestationspring.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

// http:localhost:8080/auth/login
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final ProductService productService;
    private final PersonService personService;

    @Autowired
    public AuthController(PersonValidator personValidator, ProductService productService, PersonService personService) {
        this.personValidator = personValidator;
        this.productService = productService;
        this.personService = personService;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("products", productService.getAllProduct());

        return "login";
    }
    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("person", new Person());

        return "registration";
    }
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "registration";
        }
        personService.register(person);

        return "redirect:/index";
    }
}
