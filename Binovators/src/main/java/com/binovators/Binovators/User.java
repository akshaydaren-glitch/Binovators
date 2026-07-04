package com.binovators.Binovators;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    @OneToMany(mappedBy = "user")
    private List<Animal> animals = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User() {
    }

    // ---------- GETTERS ----------

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public User getUser() {
    return user;
    }


    // ---------- SETTERS ----------

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void setUser(User user) {
    this.user = user;
    }
}