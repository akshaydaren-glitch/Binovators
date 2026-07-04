package com.binovators.Binovators;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(

            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password

    ) {

        User user = new User();

        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole("USER");

        userRepository.save(user);

        return "redirect:/login";
    }

}