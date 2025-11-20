package com.microservice1.microservice1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String helloWorld() {
        return "Hello World from Microservice 1! - UAT";
    }
}