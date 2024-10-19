package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.config.JwtProvider;
import com.jt.borrownetapi.dto.LoginRequest;
import com.jt.borrownetapi.dto.UserDTO;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.entity.UserPreferences;
import com.jt.borrownetapi.model.AuthResponse;
import com.jt.borrownetapi.model.ExtendedUsernamePasswordAuthenticationToken;
import com.jt.borrownetapi.repository.UserPreferencesRepository;
import com.jt.borrownetapi.repository.UserRepository;
import com.jt.borrownetapi.service.PreferencesService;
import com.jt.borrownetapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService customUserDetails;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;


    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody UserDTO user) throws IOException {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Date date = user.getDate();

        User isEmailExist = userRepository.findByEmailIgnoreCase(email);
        if (isEmailExist != null) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("User account already exists. Please sign in.");
            authResponse.setStatus(false);
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
        //if we need to add authentication before the save attempt, I do not think it is needed
        boolean isValidPassword = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,64}$")
                .matcher(password)
                .find();
        if (!isValidPassword) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Invalid Password: Password must be: 8 to 64 characters and contain at least one of each of the following: lowercase letter, UPPERCASE LETTER, Digit (0-9), Special character (#?!@$%^&*-)");
            authResponse.setStatus(false);
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setDate(date);

        UserPreferences userPreferences = new UserPreferences();
        if (user.getUserPreferences() != null) {
            userPreferences.setBorrowDistanceKM(user.getUserPreferences().getBorrowDistanceKM());
            userPreferences.setProfileDescription(PreferencesService.resizeImage(user.getUserPreferences().getProfileDescription()));
            userPreferences.setProfilePicture(user.getUserPreferences().getProfilePicture());
        }
        userPreferences = userPreferencesRepository.save(userPreferences);
        createdUser.setUserPreferences(userPreferences);

        User savedUser = userRepository.save(createdUser);
        ExtendedUsernamePasswordAuthenticationToken authentication = new ExtendedUsernamePasswordAuthenticationToken(email, password, firstName, lastName);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Registration Successful");
        authResponse.setStatus(true);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }





    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        ExtendedUsernamePasswordAuthenticationToken authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login success");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }




    private ExtendedUsernamePasswordAuthenticationToken authenticate(String email, String password) {


        User userDetails = customUserDetails.loadUserByUsername(email);


        if(userDetails == null) {
            throw new BadCredentialsException("Invalid email and password");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");

        }
        return new ExtendedUsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities(), userDetails.getFirstName(), userDetails.getLastName());

    }



}
