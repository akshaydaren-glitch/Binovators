
package com.binovators.Binovators;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {  
    return userRepository.findAll();
    }


    @PostMapping("/users")
    public String addUser(@RequestParam String name) {

    User user = new User();
    user.setName(name);

    userRepository.save(user);

    return "User saved successfully!";
    }

   @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {

    userRepository.deleteById(id);

    return "User deleted with id: " + id;
    }
    
   @PutMapping("/users/{id}")
public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @RequestBody User updatedUser) {

    Optional<User> optionalUser = userRepository.findById(id);

    if (optionalUser.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    User user = optionalUser.get();
    user.setName(updatedUser.getName());

    return ResponseEntity.ok(userRepository.save(user));
}

}