package com.jt.borrownetapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.model.ExtendedUsernamePasswordAuthenticationToken;
import com.jt.borrownetapi.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtUtil;
    private final ObjectMapper mapper;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private UserService customUserDetails;

    public JwtAuthorizationFilter(JwtProvider jwtUtil, ObjectMapper mapper, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null ) {
                filterChain.doFilter(request, response);
                return;
            }
            log.debug("token : "+ accessToken);
            Claims claims = jwtUtil.resolveClaims(request);

            if(claims != null & jwtUtil.validateClaims(claims)) {
                String email = (String) claims.get("email");
                log.debug("email : " + email);
                User userDetails = customUserDetails.loadUserByUsername(email);
                ExtendedUsernamePasswordAuthenticationToken authentication =
                        new ExtendedUsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities(), userDetails.getFirstName(), userDetails.getLastName());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }catch (Exception e){
            handlerExceptionResolver.resolveException(request, response, null, e);

        }
        filterChain.doFilter(request, response);
    }
}
