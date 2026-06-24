package com.binovators.Binovators;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Binovators! We are a pet adoption agency dedicated to finding loving homes for animals in need. Explore our website to learn more about our mission, view available pets, and find out how you can help make a difference in the lives of these wonderful creatures.";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World ?????";
    }
}
