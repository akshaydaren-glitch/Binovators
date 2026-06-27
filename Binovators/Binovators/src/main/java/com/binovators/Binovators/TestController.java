package com.binovators.Binovators;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "API is working";
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello from the API!";
    }
}
