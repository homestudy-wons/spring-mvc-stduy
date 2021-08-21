package com.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {

    @GetMapping("/")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "index";
    }

    @GetMapping("/hello-thymeleaf")
    public String hello(Model model) {
        model.addAttribute("name", "dsunni");
        return "hello";
    }
}
