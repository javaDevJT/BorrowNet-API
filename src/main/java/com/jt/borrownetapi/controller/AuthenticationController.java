package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.config.JwtProvider;
import com.jt.borrownetapi.dto.UserDTO;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.entity.UserPreferences;
import com.jt.borrownetapi.entity.UserPreferencesRepository;
import com.jt.borrownetapi.model.AuthResponse;
import com.jt.borrownetapi.repository.UserRepository;
import com.jt.borrownetapi.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private UserService userService;
    @Autowired
    private UserPreferencesRepository userPreferencesRepository;


    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody UserDTO user)  {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new BadCredentialsException("User account already exists. Please sign in.");
        }
        boolean isMatch = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,64}$")
                .matcher(password)
                .find();

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setPassword(passwordEncoder.encode(password));

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setBorrowDistanceKM(user.getUserPreferences().getBorrowDistanceKM());
        userPreferences.setProfileDescription(user.getUserPreferences().getProfileDescription());
        userPreferences.setProfilePicture(user.getUserPreferences().getProfilePicture());

        userPreferences = userPreferencesRepository.save(userPreferences);
        createdUser.setUserPreferences(userPreferences);

        User savedUser = userRepository.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }





    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login success");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }




    private Authentication authenticate(String email, String password) {


        UserDetails userDetails = customUserDetails.loadUserByUsername(email);


        if(userDetails == null) {

            throw new BadCredentialsException("Invalid email and password");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }



}
