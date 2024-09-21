package com.jt.borrownetapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class JwtProvider {
    static SecretKey key = Keys.hmacShaKeyFor("7C634156586893EA277F0D44BAAA6CD99CA3FC5476C80265AA3DB0C77ADFC511".getBytes());

    public static String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);
        @SuppressWarnings("deprecation")
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email", auth.getName())
                .claim( "authorities",roles)
                .signWith(key)
                .compact();
        log.debug("Token for parsing in JwtProvider: " + jwt);
        return jwt;

    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for(GrantedAuthority authority: authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",",auths);
    }


    @SuppressWarnings("deprecation")
    public static String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7); // Assuming "Bearer " is removed from the token
        try {
            //Claims claims=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
            Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
            String email = String.valueOf(claims.get("email"));
            log.debug("Email extracted from JWT: " + claims);
            return email;
        } catch (Exception e) {
            System.err.println("Error extracting email from JWT: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}