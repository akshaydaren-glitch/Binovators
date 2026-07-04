
package com.binovators.Binovators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/",
                        "/login",
                        "/register",
                        "/css/**",
                        "/style.css",
                        "/script.js",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()

                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                .requestMatchers("/animals/delete/**")
                .hasAnyRole("MOD","ADMIN","USER")

                .requestMatchers("/animals/**")
                .authenticated()

                .anyRequest()
                .authenticated()
            )

            .formLogin(form -> form

                .loginPage("/login")

                .defaultSuccessUrl("/animals/page", true)

                .permitAll()
            )

            .logout(logout -> logout

                .logoutSuccessUrl("/login")

                .permitAll()

            );

        return http.build();
    }
}