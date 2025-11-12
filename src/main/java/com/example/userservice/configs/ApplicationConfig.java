package com.example.userservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpsecurity) throws Exception {
        httpsecurity.csrf().disable();
        httpsecurity.cors().disable();
        httpsecurity.authorizeHttpRequests(
         //authorize->authorize.requestMatchers("/signup").permitAll()
              authorize->  authorize.anyRequest().permitAll()
        );
        return httpsecurity.build();
    }
}
