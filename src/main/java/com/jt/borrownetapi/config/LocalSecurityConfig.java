package com.jt.borrownetapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@Profile({/*"!cloud", */"local"}) //explicitly require local for this to activate for now
@EnableMethodSecurity(prePostEnabled = true)
public class LocalSecurityConfig {

    @Bean
    public SecurityFilterChain borrowNetSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.warn("Security is disabled in local/!cloud profile!");
        httpSecurity
                .authorizeHttpRequests(req -> req.anyRequest().permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
}
