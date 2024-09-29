package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.dto.PublicUserDTO;
import com.jt.borrownetapi.dto.UserDTO;
import com.jt.borrownetapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("public/{id}")
    public PublicUserDTO getPublicUser(@PathParam("id") Integer id) {
        PublicUserDTO foundUser = userService.getPublicUserById(id);
        if (foundUser == null) {
            throw new EntityNotFoundException("User does not exist.");
        } else {
            return foundUser;
        }
    }

    @GetMapping
    public UserDTO getPrivateUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return userService.getPrivateUserDTO();
        }
    }
}