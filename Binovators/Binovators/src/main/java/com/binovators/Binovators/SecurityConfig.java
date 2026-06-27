package com.binovators.Binovators;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            
                
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login").permitAll()
                
                
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/animals/delete/**").hasRole("MOD")
                .requestMatchers("/animals/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/animals/page", true)
            
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
            )
            
            //.csrf(csrf -> csrf.disable())
            ;

        return http.build();
    }
}