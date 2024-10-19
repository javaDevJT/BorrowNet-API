package com.jt.borrownetapi.config;

import com.jt.borrownetapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.OPTIONS;

@Slf4j
@Configuration
@EnableWebSecurity
@Profile("!local"/*{"cloud"}*/) //always enable cloud for now
@EnableMethodSecurity(prePostEnabled = true)
public class CloudSecurityConfig {

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

    //add more here if we want to make security work
    @Bean
    public SecurityFilterChain borrowNetSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Security is enabled - Endpoints secured.");
        httpSecurity
                .authorizeHttpRequests(req -> req.requestMatchers("/", "/auth/**").permitAll())
                .authorizeHttpRequests(req -> req.requestMatchers(OPTIONS, "/**").permitAll())
                .authorizeHttpRequests(req -> req.anyRequest().authenticated())
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserService userService) {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
