package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.dto.UserPreferencesDTO;
import com.jt.borrownetapi.service.PreferencesService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/preferences")
public class PreferencesController {

    @Autowired
    private PreferencesService preferencesService;

    @GetMapping
    public UserPreferencesDTO getUserPreferences() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return preferencesService.getPrivateUserPreferencesDTO();
        }
    }

    @PatchMapping
    @Transactional
    public ResponseEntity<UserPreferencesDTO> updateUserPreferences(@Valid @RequestBody UserPreferencesDTO userPreferencesDTO) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(preferencesService.updateUserPreferences(userPreferencesDTO),
                    HttpStatusCode.valueOf(HttpStatus.ACCEPTED.value()));
        }
    }
}
