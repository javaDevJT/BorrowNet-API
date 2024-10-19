package com.jt.borrownetapi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class ExtendedUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String firstName;
    private String lastName;

    public ExtendedUsernamePasswordAuthenticationToken(Object principal, Object credentials, String firstName, String lastName) {
        super(principal, credentials);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ExtendedUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String firstName, String lastName) {
        super(principal, credentials, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
