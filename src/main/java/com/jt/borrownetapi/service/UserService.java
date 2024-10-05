package com.jt.borrownetapi.service;


import com.jt.borrownetapi.dto.PublicUserDTO;
import com.jt.borrownetapi.dto.UserDTO;
import com.jt.borrownetapi.dto.UserPreferencesDTO;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email);
        log.debug(String.valueOf(user));

        if(user==null) {
            throw new UsernameNotFoundException("User not found with this email " + email);

        }


        log.debug("Loaded user: " + user.getEmail() + ", Role: " + user.getRole());
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    public PublicUserDTO getPublicUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return PublicUserDTO.builder().email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .id(user.getId())
                    .build();
        } else {
            return null;
        }
    }

    public UserDTO getPrivateUserDTO() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        return UserDTO.builder().id(userByEmail.getId())
                .email(userByEmail.getEmail())
                .firstName(userByEmail.getFirstName())
                .lastName(userByEmail.getLastName())
                .role(userByEmail.getRole())
                .userPreferences(UserPreferencesDTO.builder()
                        .id(userByEmail.getUserPreferences().getId())
                        .borrowDistanceKM(userByEmail.getUserPreferences().getBorrowDistanceKM())
                        .profilePicture(userByEmail.getUserPreferences().getProfilePicture())
                        .profileDescription(userByEmail.getUserPreferences().getProfileDescription())
                        .build())
                .build();
    }
}
