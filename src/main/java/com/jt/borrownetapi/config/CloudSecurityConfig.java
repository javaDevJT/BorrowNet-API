package com.jt.borrownetapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@Profile({"cloud"})
@EnableMethodSecurity(prePostEnabled = true)
public class CloudSecurityConfig {

    //add more here if we want to make security work
    @Bean
    public SecurityFilterChain borrowNetSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Security is enabled - Endpoints secured.");
        httpSecurity
                .authorizeHttpRequests(req -> req.requestMatchers("/", "").permitAll())
                .authorizeHttpRequests(req -> req.anyRequest().authenticated());
        return httpSecurity.build();
    }
}
