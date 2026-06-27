
package com.binovators.Binovators;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Controller
public class UserController {

    private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
}

    
    /*@PostMapping("/register")
public String register(
        @RequestParam String name,
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password) {

    User user = new User();
    user.setName(name);
    user.setUsername(username);
    user.setEmail(email);

    // 🔥 THIS IS REQUIRED
    user.setPassword(passwordEncoder.encode(password));
            System.out.println("REGISTER HIT");
            User saved = userRepository.save(user);
System.out.println(saved.getId());
    user.setRole("USER");

    userRepository.save(user);

    return "redirect:/login";
}   
    @GetMapping("/users")
    public List<User> getUsers() {  
    return userRepository.findAll();
    }*/

  @PostMapping("/register")
@ResponseBody
public String registerTest() {
    return "REGISTER WORKS";
}

   

  
}

